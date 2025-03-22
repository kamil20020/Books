package pl.books.magagement.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.books.magagement.model.entity.AuthorEntity;
import pl.books.magagement.model.entity.BookEntity;
import pl.books.magagement.model.entity.PublisherEntity;
import pl.books.magagement.model.internal.CreateBook;
import pl.books.magagement.repository.AuthorRepository;
import pl.books.magagement.repository.BookRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

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
    public void deleteById(UUID bookId) throws EntityNotFoundException{

        BookEntity foundBook = bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("Book was not found by given id"));

        foundBook.getAuthors().forEach(author -> {

            int newPublishedBooksCount = author.getPublishedBooksCount() - 1;

            author.setPublishedBooksCount((short) newPublishedBooksCount);
            author.getBooks().remove(foundBook);
        });

        bookRepository.deleteById(bookId);
    }
}
