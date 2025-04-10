package pl.books.magagement.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import pl.books.magagement.model.entity.PublisherEntity;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class AuthorRepositoryTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:13.0");

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @BeforeEach
    public void setUp(){

        authorRepository.deleteAll();
    }

    @Test
    void shouldFindAllByMainPublisherId() {
        
        //given
        PublisherEntity publisher = PublisherEntity.builder()
            .name("Publisher 123")
            .build();

        publisher = publisherRepository.save(publisher);

        AuthorEntity a1 = AuthorEntity.builder()
            .firstname("Kamil")
            .surname("Nowak")
            .publishedBooksCount((short) 0)
            .books(new HashSet<>())
            .mainPublisher(publisher)
            .build();

        AuthorEntity a2 = AuthorEntity.builder()
            .firstname("Adam")
            .surname("Kowalski")
            .publishedBooksCount((short) 0)
            .books(new HashSet<>())
            .mainPublisher(publisher)
            .build();

        AuthorEntity a3 = AuthorEntity.builder()
            .firstname("Michał")
            .surname("Nowak")
            .publishedBooksCount((short) 0)
            .books(new HashSet<>())
            .build();

        a1 = authorRepository.save(a1);
        a2 = authorRepository.save(a2);
        a3 = authorRepository.save(a3);

        Pageable pageable = PageRequest.of(0, 5);

        //when
        Page<AuthorEntity> gotAuthorsPage = authorRepository.findAllByMainPublisherId(publisher.getId(), pageable);

        List<AuthorEntity> gotAuthors = gotAuthorsPage.getContent();

        //then
        assertEquals(2, gotAuthorsPage.getTotalElements());
        assertTrue(gotAuthors.contains(a1));
        assertTrue(gotAuthors.contains(a2));
    }
}