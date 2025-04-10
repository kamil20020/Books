package pl.books.magagement.controller.integration;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.books.magagement.model.api.request.CreatePublisherRequest;
import pl.books.magagement.model.api.response.AuthorHeader;
import pl.books.magagement.model.api.response.BookResponse;
import pl.books.magagement.model.api.response.PublisherHeader;
import pl.books.magagement.model.api.response.RestPage;
import pl.books.magagement.model.entity.*;
import pl.books.magagement.model.mappers.AuthorMapper;
import pl.books.magagement.model.mappers.BookMapper;
import pl.books.magagement.model.mappers.PublisherMapper;
import pl.books.magagement.repository.AuthorRepository;
import pl.books.magagement.repository.BookRepository;
import pl.books.magagement.repository.PublisherRepository;
import pl.books.magagement.repository.UserRepository;
import pl.books.magagement.service.RoleService;
import pl.books.magagement.service.UserService;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PublisherControllerTestIT {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");

    @LocalServerPort
    public int port;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PublisherMapper publisherMapper;

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private BookMapper bookMapper;

    @BeforeEach
    public void setUp(){

        bookRepository.deleteAll();
        authorRepository.deleteAll();
        publisherRepository.deleteAll();
        userRepository.deleteAll();

        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost/publishers";
    }

    public String createAdminAndGetAccessToken(){

        String username = "kamil";
        String password = "nowak";

        UserEntity user = userService.register(username, password);

        RoleEntity role = roleService.createRole("ADMIN");

        roleService.grantRole(user.getId(), role.getId());

        return userService.login(username, password).accessToken();
    }

    @Test
    void shouldGetPage() {

        //given
        PublisherEntity publisher = new PublisherEntity("Publisher 123");
        PublisherEntity publisher1 = new PublisherEntity("Publisher 1234");

        publisher = publisherRepository.save(publisher);
        publisher1 = publisherRepository.save(publisher1);

        Pageable pageable = PageRequest.of(0, 5);

        PublisherHeader publisherHeader = publisherMapper.publisherEntityToPublisherHeader(publisher);
        PublisherHeader publisherHeader1 = publisherMapper.publisherEntityToPublisherHeader(publisher1);

        //when
        Page<PublisherHeader> gotPublishersPage = RestAssured
        .given()
            .param("page", pageable.getPageNumber())
            .param("size", pageable.getPageSize())
        .when()
            .get()
        .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<RestPage<PublisherHeader>>(){});

        //then
        assertEquals(2, gotPublishersPage.getTotalElements());
        assertEquals(0, gotPublishersPage.getNumber());
        assertEquals(5, gotPublishersPage.getSize());
        assertTrue(gotPublishersPage.getContent().contains(publisherHeader));
        assertTrue(gotPublishersPage.getContent().contains(publisherHeader1));
    }

    @Test
    void shouldGetPublisherAuthors() {
        
        //given
        PublisherEntity publisher = new PublisherEntity("Publisher 123");
        
        publisher = publisherRepository.save(publisher);
        
        AuthorEntity author = AuthorEntity.builder()
            .firstname("Kamil")
            .surname("Nowak")
            .publishedBooksCount((short) 0)
            .mainPublisher(publisher)
            .books(new HashSet<>())
            .build();

        AuthorEntity author1 = AuthorEntity.builder()
            .firstname("Adam")
            .surname("Kowalski")
            .publishedBooksCount((short) 0)
            .books(new HashSet<>())
            .build();
        
        authorRepository.save(author);
        authorRepository.save(author1);

        Pageable pageable = PageRequest.of(0, 5);

        AuthorHeader authorHeader = authorMapper.authorEntityToAuthorHeader(author);
        
        //when
        Page<AuthorHeader> gotAuthorsHeadersPage = RestAssured
        .given()
            .param("page", pageable.getPageNumber())
            .param("size", pageable.getPageSize())
        .when()
            .pathParam("publisherId", publisher.getId())
            .get("/{publisherId}/authors")
        .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<RestPage<AuthorHeader>>(){});

        List<AuthorHeader> gotAuthorsHeaders = gotAuthorsHeadersPage.getContent();

        //then
        assertEquals(1, gotAuthorsHeadersPage.getTotalElements());
        assertEquals(0, gotAuthorsHeadersPage.getNumber());
        assertEquals(5, gotAuthorsHeadersPage.getSize());
        assertTrue(gotAuthorsHeaders.contains(authorHeader));
    }

    @Test
    void shouldGetPublisherBooks() {

        //given
        PublisherEntity publisher = new PublisherEntity("Publisher 123");
        PublisherEntity publisher1 = new PublisherEntity("Publisher 1234");

        publisher = publisherRepository.save(publisher);
        publisher1 = publisherRepository.save(publisher1);

        AuthorEntity author = AuthorEntity.builder()
            .firstname("Kamil")
            .surname("Nowak")
            .publishedBooksCount((short) 2)
            .books(new HashSet<>())
            .build();

        author = authorRepository.save(author);

        BookEntity book = BookEntity.builder()
            .title("Book 123")
            .publicationDate(LocalDate.now())
            .price(BigDecimal.valueOf(2248, 2))
            .picture("raw picture".getBytes(StandardCharsets.UTF_8))
            .publisher(publisher)
            .authors(List.of(author))
            .build();

        BookEntity book1 = BookEntity.builder()
            .title("Book 1234")
            .publicationDate(LocalDate.now())
            .price(BigDecimal.valueOf(2448, 2))
            .picture("raw picture1".getBytes(StandardCharsets.UTF_8))
            .publisher(publisher1)
            .authors(List.of(author))
            .build();

        book = bookRepository.save(book);
        book1 = bookRepository.save(book1);

        author.getBooks().add(book);
        author.getBooks().add(book1);

        Pageable pageable = PageRequest.of(0, 5);

        BookResponse bookResponse = bookMapper.bookEntityToBookResponse(book);

        //when
        Page<BookResponse> gotBooksResponsesPage = RestAssured
        .given()
            .param("page", pageable.getPageNumber())
            .param("size", pageable.getPageSize())
        .when()
            .get("/{publisherId}/books", publisher.getId())
        .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<RestPage<BookResponse>>(){});

        List<BookResponse> gotBooksResponses = gotBooksResponsesPage.getContent();

        //then
        assertEquals(1, gotBooksResponsesPage.getTotalElements());
        assertEquals(0, gotBooksResponsesPage.getNumber());
        assertEquals(5, gotBooksResponsesPage.getSize());
        assertTrue(gotBooksResponses.contains(bookResponse));
    }

    @Test
    void shouldCreatePublisher() {

        //given
        CreatePublisherRequest request = new CreatePublisherRequest("Publisher 123");

        String accessToken = createAdminAndGetAccessToken();

        //when
        PublisherHeader gotPublisherHeader = RestAssured
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
            .as(PublisherHeader.class);

        //then
        assertNotNull(gotPublisherHeader);
        assertNotNull(gotPublisherHeader.id());

        UUID gotPublisherId = UUID.fromString(gotPublisherHeader.id());

        Optional<PublisherEntity> foundPublisherOpt = publisherRepository.findById(gotPublisherId);

        assertTrue(foundPublisherOpt.isPresent());

        PublisherEntity foundPublisher = foundPublisherOpt.get();

        assertEquals(foundPublisher.getId(), gotPublisherId);
        assertEquals(foundPublisher.getName(), request.name());
        assertEquals(request.name(), request.name());
    }

    @Test
    public void shouldNotCreateDuplicatePublisher(){

        //given
        PublisherEntity publisher = new PublisherEntity("Publisher 123");

        publisher = publisherRepository.save(publisher);

        CreatePublisherRequest request = new CreatePublisherRequest(publisher.getName());

        String accesssToken = createAdminAndGetAccessToken();

        //when
        RestAssured
        .given()
            .contentType(ContentType.JSON)
            .body(request)
            .header(new Header(
                HttpHeaders.AUTHORIZATION,
                "Bearer " + accesssToken
            ))
        .when()
            .post()
        .then()
            .statusCode(409);

        //then
    }

    @Test
    void shouldDeletePublisherById() {

        //given
        PublisherEntity publisher = new PublisherEntity("Publisher 123");

        publisher = publisherRepository.save(publisher);

        String accessToken = createAdminAndGetAccessToken();

        //when
        RestAssured
        .given()
            .header(new Header(
                HttpHeaders.AUTHORIZATION,
                "Bearer " + accessToken
            ))
        .when()
            .delete("/{publisherId}", publisher.getId())
        .then()
            .statusCode(204);

        //then
    }
}