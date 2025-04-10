package pl.books.magagement.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.books.magagement.model.entity.AuthorEntity;
import pl.books.magagement.model.entity.BookEntity;
import pl.books.magagement.model.entity.PublisherEntity;
import pl.books.magagement.model.internal.CreateAuthor;
import pl.books.magagement.repository.AuthorRepository;
import pl.books.magagement.repository.BookRepository;
import pl.books.magagement.repository.PublisherRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    private final PublisherService publisherService;

    public AuthorEntity getById(UUID authorId) throws EntityNotFoundException{

        return authorRepository.findById(authorId)
            .orElseThrow(() -> new EntityNotFoundException("Author was not found by given id"));
    }

    public Page<AuthorEntity> getPage(Pageable pageable){

        if(pageable == null){
            pageable = Pageable.unpaged();
        }

        return authorRepository.findAll(pageable);
    }

    public Page<BookEntity> getAuthorBooksPage(UUID authorId, Pageable pageable) throws EntityNotFoundException{

        if(pageable == null){
            pageable = Pageable.unpaged();
        }

        if(!authorRepository.existsById(authorId)){
            throw new EntityNotFoundException("Author was not found by given id");
        }

        return bookRepository.findAllByAuthorsIdIn(List.of(authorId), pageable);
    }

    @Transactional
    public AuthorEntity create(CreateAuthor toCreate) throws EntityNotFoundException{

        AuthorEntity newAuthor = AuthorEntity.builder()
            .firstname(toCreate.firstname())
            .surname(toCreate.surname())
            .publishedBooksCount((short) 0)
        .build();

        UUID mainPublisherId = toCreate.mainPublisherId();

        if(mainPublisherId != null){

            PublisherEntity mainPublisher = publisherService.getById(mainPublisherId);

            newAuthor.setMainPublisher(mainPublisher);
        }

        return authorRepository.save(newAuthor);
    }

    @Transactional
    public void deleteById(UUID authorId) throws EntityNotFoundException{

        Optional<AuthorEntity> gotAuthorOpt = authorRepository.findById(authorId);

        if(gotAuthorOpt.isEmpty()){
            throw new EntityNotFoundException("Author was not found by given id");
        }

        AuthorEntity gotAuthor = gotAuthorOpt.get();

        gotAuthor.getBooks().forEach(book -> {
            book.getAuthors().remove(gotAuthor);
        });

        authorRepository.deleteById(authorId);
    }
}
