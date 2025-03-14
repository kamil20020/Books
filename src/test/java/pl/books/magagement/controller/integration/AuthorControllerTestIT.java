package pl.books.magagement.controller.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.books.magagement.config.JwtFilter;
import pl.books.magagement.config.JwtService;
import pl.books.magagement.model.api.request.CreateAuthorRequest;
import pl.books.magagement.model.api.response.AuthorHeader;
import pl.books.magagement.model.entity.AuthorEntity;
import pl.books.magagement.model.entity.PublisherEntity;
import pl.books.magagement.model.entity.UserEntity;
import pl.books.magagement.repository.AuthorRepository;
import pl.books.magagement.repository.PublisherRepository;
import pl.books.magagement.repository.UserRepository;
import pl.books.magagement.service.UserService;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class AuthorControllerTestIT {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:13.0");

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    public void setUp(){

        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost/authors";

        authorRepository.deleteAll();
        publisherRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldGetPage() {

        //given

        //when

        //then
    }

    @Test
    void shouldGetAuthorBooksPage() {


    }

    @Test
    void shouldCreate() {

        //given
        String username = "adam";
        String rawPassword = "nowak";

        userService.register(username, rawPassword);
        String accessToken = userService.login(username, rawPassword).accessToken();

        PublisherEntity publisher = PublisherEntity.builder()
            .name("Publisher 123")
            .build();

        publisher = publisherRepository.save(publisher);

        CreateAuthorRequest request = new CreateAuthorRequest(
            "Adam",
            "Nowak",
            publisher.getId().toString()
        );

        //when
        AuthorHeader gotAuthorHeader = RestAssured
            .given()
                .contentType(ContentType.JSON)
                .body(request)
                .header(new Header(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer " + accessToken
                ))
            .when()
                .post()
            .then()
                .statusCode(201)
                .extract()
                .as(AuthorHeader.class);

        AuthorEntity createdAuthor = authorRepository.findAll().iterator().next();

        //then
        assertNotNull(createdAuthor);
        assertEquals(createdAuthor.getId().toString(), gotAuthorHeader.id());
        assertEquals(createdAuthor.getFirstname(), gotAuthorHeader.firstname());
        assertEquals(createdAuthor.getSurname(), gotAuthorHeader.surname());
        assertEquals(createdAuthor.getMainPublisher().getId().toString(), gotAuthorHeader.mainPublisher().id());
        assertEquals(createdAuthor.getPublishedBooksCount(), gotAuthorHeader.publishedBooksCount());
    }

    @Test
    void shouldDeleteById() {

        //given
        String username = "adam";
        String password = "nowak";

        userService.register(username, password);
        String accessToken = userService.login(username, password).accessToken();

        AuthorEntity author = AuthorEntity.builder()
            .firstname("Adam")
            .surname("Nowak")
            .publishedBooksCount((short) 0)
            .books(new HashSet<>())
            .mainPublisher(null)
            .build();

        author = authorRepository.save(author);

        //when
        RestAssured
            .given()
                .header(
                    new Header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                )
            .when()
                .delete("/{authorId}", author.getId().toString())
            .then()
                .statusCode(204);

        //then
        assertEquals(0, authorRepository.count());
    }
}