package pl.books.magagement.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.books.magagement.model.entity.AuthorEntity;
import pl.books.magagement.model.entity.BookEntity;
import pl.books.magagement.model.entity.PublisherEntity;
import pl.books.magagement.service.AuthorService;
import pl.books.magagement.service.PublisherService;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>("postgres:13");

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @ParameterizedTest
    @CsvSource(value = {
        "Book 123, true",
        "BOOK 123, true",
        "BooK 123, true",
        "bOOk 123, true",
        "booK 123, true",
        "book 123, true",
        "book, false",
        "booK 1234, false",
        "123, false"
    })
    void shouldExistsByTitleIgnoreCase(String title, boolean expected) {

        //given
        AuthorEntity author = AuthorEntity.builder()
            .firstname("Adam")
            .surname("Nowak")
            .publishedBooksCount((short) 1)
            .books(new HashSet<>())
            .build();

        author = authorRepository.save(author);

        PublisherEntity publisher = new PublisherEntity("Publisher 123");

        publisher = publisherRepository.save(publisher);

        BookEntity book = BookEntity.builder()
            .title("Book 123")
            .price(BigDecimal.valueOf(2.35))
            .publicationDate(LocalDate.now())
            .authors(List.of(author))
            .publisher(publisher)
            .build();

        bookRepository.save(book);

        //when
        boolean actual = bookRepository.existsByTitleIgnoreCase(title);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void shouldFindAllByPublisherId() {

        AuthorEntity author = AuthorEntity.builder()
            .firstname("Adam")
            .surname("Nowak")
            .mainPublisher(null)
            .publishedBooksCount((short) 1)
            .books(new HashSet<>())
            .build();

        authorRepository.save(author);

        PublisherEntity publisher = new PublisherEntity("Publisher 123");

        publisherRepository.save(publisher);

        byte[] picture = "picture".getBytes(StandardCharsets.UTF_8);

        BookEntity book = BookEntity.builder()
            .title("Book 123")
            .price(BigDecimal.valueOf(2.35))
            .publicationDate(LocalDate.now())
            .picture(picture)
            .authors(List.of(author))
            .publisher(publisher)
            .build();

        BookEntity book1 = BookEntity.builder()
            .title("Book 1234")
            .price(BigDecimal.valueOf(20.26))
            .publicationDate(LocalDate.now())
            .picture(picture)
            .authors(List.of(author))
            .publisher(publisher)
            .build();

        book = bookRepository.save(book);
        book1 = bookRepository.save(book1);

        Pageable pageable = PageRequest.of(0, 5);

        //when
        Page<BookEntity> gotBooksPage = bookRepository.findAllByPublisherId(publisher.getId(), pageable);

        //then
        assertEquals(pageable, gotBooksPage.getPageable());
        assertEquals(2, gotBooksPage.getTotalElements());

        List<BookEntity> gotBooks = gotBooksPage.getContent();

        assertTrue(gotBooks.contains(book));
        assertTrue(gotBooks.contains(book1));
    }

    @Test
    void shouldNotFindAllByPublisherId(){

        //given
        AuthorEntity author = AuthorEntity.builder()
            .firstname("Adam")
            .surname("Nowak")
            .mainPublisher(null)
            .publishedBooksCount((short) 1)
            .books(new HashSet<>())
            .build();

        authorRepository.save(author);

        PublisherEntity publisher = new PublisherEntity("Publisher 123");

        publisherRepository.save(publisher);

        byte[] picture = "picture".getBytes(StandardCharsets.UTF_8);

        BookEntity book = BookEntity.builder()
            .title("Book 123")
            .price(BigDecimal.valueOf(2.35))
            .publicationDate(LocalDate.now())
            .picture(picture)
            .authors(List.of(author))
            .publisher(publisher)
            .build();

        book = bookRepository.save(book);

        Pageable pageable = PageRequest.of(0, 5);

        //when
        Page<BookEntity> gotBooksPage = bookRepository.findAllByPublisherId(UUID.randomUUID(), pageable);

        //then
        assertEquals(pageable, gotBooksPage.getPageable());
        assertEquals(0, gotBooksPage.getTotalElements());
    }

    @Test
    void shouldFindAllByAuthorsIdIn() {

        AuthorEntity author = AuthorEntity.builder()
            .firstname("Adam")
            .surname("Nowak")
            .mainPublisher(null)
            .publishedBooksCount((short) 1)
            .books(new HashSet<>())
            .build();

        AuthorEntity author1 = AuthorEntity.builder()
            .firstname("Kamil")
            .surname("Kowalski")
            .mainPublisher(null)
            .publishedBooksCount((short) 1)
            .books(new HashSet<>())
            .build();

        authorRepository.save(author);

        PublisherEntity publisher = new PublisherEntity("Publisher 123");

        publisherRepository.save(publisher);

        byte[] picture = "picture".getBytes(StandardCharsets.UTF_8);

        BookEntity book = BookEntity.builder()
            .title("Book 123")
            .price(BigDecimal.valueOf(2.35))
            .publicationDate(LocalDate.now())
            .picture(picture)
            .authors(List.of(author))
            .publisher(publisher)
            .build();

        BookEntity book1 = BookEntity.builder()
            .title("Book 1234")
            .price(BigDecimal.valueOf(20.26))
            .publicationDate(LocalDate.now())
            .picture(picture)
            .authors(List.of(author, author1))
            .publisher(publisher)
            .build();

        book = bookRepository.save(book);
        book1 = bookRepository.save(book1);

        Pageable pageable = PageRequest.of(0, 5);

        //when
        Page<BookEntity> gotBooksPage = bookRepository.findAllByAuthorsIdIn(List.of(author.getId()), pageable);

        //then
        assertEquals(pageable, gotBooksPage.getPageable());
        assertEquals(2, gotBooksPage.getTotalElements());

        List<BookEntity> gotBooks = gotBooksPage.getContent();

        assertTrue(gotBooks.contains(book));
        assertTrue(gotBooks.contains(book1));
    }

    @Test
    void shouldNotFindAllByAuthorIdInWhenAuthorDoesNotExist(){

        //given
        AuthorEntity author = AuthorEntity.builder()
            .firstname("Adam")
            .surname("Nowak")
            .mainPublisher(null)
            .publishedBooksCount((short) 1)
            .books(new HashSet<>())
            .build();

        authorRepository.save(author);

        PublisherEntity publisher = new PublisherEntity("Publisher 123");

        publisherRepository.save(publisher);

        byte[] picture = "picture".getBytes(StandardCharsets.UTF_8);

        BookEntity book = BookEntity.builder()
            .title("Book 123")
            .price(BigDecimal.valueOf(2.35))
            .publicationDate(LocalDate.now())
            .picture(picture)
            .authors(List.of(author))
            .publisher(publisher)
            .build();

        book = bookRepository.save(book);

        Pageable pageable = PageRequest.of(0, 5);

        //when
        Page<BookEntity> gotBooksPage = bookRepository.findAllByAuthorsIdIn(List.of(UUID.randomUUID()), pageable);

        //then
        assertEquals(pageable, gotBooksPage.getPageable());
        assertEquals(0, gotBooksPage.getTotalElements());
    }
}