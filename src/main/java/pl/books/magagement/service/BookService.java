package pl.books.magagement.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.books.magagement.model.api.request.BookSearchCriteriaRequest;
import pl.books.magagement.model.api.response.PatchBook;
import pl.books.magagement.model.entity.AuthorEntity;
import pl.books.magagement.model.entity.BookEntity;
import pl.books.magagement.model.entity.PublisherEntity;
import pl.books.magagement.model.internal.BookSearchCriteria;
import pl.books.magagement.model.internal.CreateBook;
import pl.books.magagement.repository.AuthorRepository;
import pl.books.magagement.repository.BookRepository;
import pl.books.magagement.specification.BookSpecification;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.springframework.data.jpa.domain.Specification.*;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    private final PublisherService publisherService;

    public Page<BookEntity> getBooksPage(Pageable pageable){

        if(pageable == null){
            pageable = Pageable.unpaged();
        }

        return bookRepository.findAll(pageable);
    }

    public Page<BookEntity> search(BookSearchCriteria criteria, Pageable pageable){

        boolean isCriteriaEmpty = Stream.of(criteria.title(), criteria.authorName(), criteria.publisherName(), criteria.publicationDate(), criteria.price())
            .allMatch(Objects::isNull);

        if(isCriteriaEmpty){
            return getBooksPage(pageable);
        }

        if(pageable == null){
            pageable = Pageable.unpaged();
        }

        return bookRepository.findAll(
            where(
                allOf(
                    anyOf(
                        BookSpecification.matchTitle(criteria.title()),
                        BookSpecification.matchAuthorName(criteria.authorName()),
                        BookSpecification.matchPublisherName(criteria.publisherName())
                    ),
                    BookSpecification.matchLessPublicationDate(criteria.publicationDate()),
                    BookSpecification.matchLessPrice(criteria.price())
                )
            ),
            pageable
        );
    }

    private BookEntity getById(UUID bookId) throws EntityNotFoundException{

        return bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("Book was not found with given id"));
    }

    @Transactional
    public BookEntity create(CreateBook createBook) throws EntityNotFoundException, EntityExistsException{

        PublisherEntity publisher = null;

        if(createBook.publisherId() != null){
            publisher = publisherService.getById(createBook.publisherId());
        }

        List<AuthorEntity> authors = authorRepository.findAllById(createBook.authorsIds());

        if(bookRepository.existsByTitleIgnoreCase(createBook.title())){
           throw new EntityExistsException("Duplicate title");
        }

        BookEntity toCreateBook = BookEntity.builder()
            .title(createBook.title())
            .publicationDate(createBook.publicationDate())
            .price(createBook.price())
            .publisher(publisher)
            .authors(authors)
            .picture(createBook.picture())
            .build();

        toCreateBook = bookRepository.save(toCreateBook);

        BookEntity finalToCreateBook = toCreateBook;

        authors.forEach(author -> {

            int newPublishedBooksCount = author.getPublishedBooksCount() + 1;

            author.setPublishedBooksCount((short) newPublishedBooksCount);
            author.getBooks().add(finalToCreateBook);
        });

        return toCreateBook;
    }

    @Transactional
    public BookEntity patchById(UUID bookId, PatchBook patchBook) throws EntityNotFoundException{

        BookEntity foundBook = getById(bookId);

        if(patchBook.title() != null){
            foundBook.setTitle(patchBook.title());
        }

        if(patchBook.price() != null){
            foundBook.setPrice(patchBook.price());
        }

        if(patchBook.picture() != null){
            foundBook.setPicture(patchBook.picture());
        }

        return foundBook;
    }

    @Transactional
    public void deleteById(UUID bookId) throws EntityNotFoundException{

        BookEntity foundBook = getById(bookId);

        foundBook.getAuthors().forEach(author -> {

            int newPublishedBooksCount = author.getPublishedBooksCount() - 1;

            author.setPublishedBooksCount((short) newPublishedBooksCount);
            author.getBooks().remove(foundBook);
        });

        bookRepository.deleteById(bookId);
    }
}
