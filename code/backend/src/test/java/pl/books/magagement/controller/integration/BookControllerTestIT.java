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
import pl.books.magagement.config.JwtService;
import pl.books.magagement.model.api.request.CreateBookRequest;
import pl.books.magagement.model.api.response.BookResponse;
import pl.books.magagement.model.api.response.RestPage;
import pl.books.magagement.model.entity.*;
import pl.books.magagement.model.mappers.BookMapper;
import pl.books.magagement.repository.*;
import pl.books.magagement.service.RoleService;
import pl.books.magagement.service.UserService;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class BookControllerTestIT {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQL = new PostgreSQLContainer<>("postgres:13");

    @LocalServerPort
    private int port;

    @Autowired
    public BookRepository bookRepository;

    @Autowired
    public AuthorRepository authorRepository;

    @Autowired
    public PublisherRepository publisherRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public RoleRepository roleRepository;

    @Autowired
    public UserService userService;

    @Autowired
    public RoleService roleService;

    @Autowired
    public BookMapper bookMapper;

    @BeforeEach
    public void setUp(){

        RestAssured.baseURI = "http://localhost/books";
        RestAssured.port = port;

        bookRepository.deleteAll();
        authorRepository.deleteAll();
        publisherRepository.deleteAll();
        userRepository.deleteAll();
    }

    public String createAdminAndGetAccessToken(String username, String password){

        UserEntity user = userService.register(username, password);

        RoleEntity role = roleService.createRole("ADMIN");

        roleService.grantRole(user.getId(), role.getId());

        return userService.login(username, password).accessToken();
    }

    @Test
    public void shouldGetBooksPage(){

        //given
        PublisherEntity publisher = new PublisherEntity("Publisher 123");

        publisher = publisherRepository.save(publisher);

        AuthorEntity author = AuthorEntity.builder()
            .firstname("Adam")
            .surname("Nowak")
            .books(new HashSet<>())
            .publishedBooksCount((short) 1)
            .mainPublisher(publisher)
            .build();

        author = authorRepository.save(author);

        BookEntity book = BookEntity.builder()
            .title("Book 123")
            .picture("Picture 123".getBytes(StandardCharsets.UTF_8))
            .authors(List.of(author))
            .publicationDate(LocalDate.now())
            .price(BigDecimal.valueOf(2.35))
            .publisher(publisher)
            .build();

        book = bookRepository.save(book);

        BookEntity book1 = BookEntity.builder()
            .title("Book 1234")
            .picture("Picture 1234".getBytes(StandardCharsets.UTF_8))
            .authors(List.of(author))
            .publicationDate(LocalDate.now())
            .price(BigDecimal.valueOf(34.25))
            .publisher(publisher)
            .build();

        book1 = bookRepository.save(book1);

        author.getBooks().add(book);
        author.getBooks().add(book1);

        Pageable pageable = PageRequest.of(0, 5);

        List<BookResponse> expectedBooksResponses = List.of(
            bookMapper.bookEntityToBookResponse(book),
            bookMapper.bookEntityToBookResponse(book1)
        );

        //when
        Page<BookResponse> gotBooksResponsesPage = RestAssured
        .given()
            .param("page", pageable.getPageNumber())
            .param("pageSize", pageable.getPageSize())
        .when()
            .get()
        .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<RestPage<BookResponse>>(){});

        //then
        assertEquals(2, gotBooksResponsesPage.getTotalElements());

        List<BookResponse> gotBooksResponses = gotBooksResponsesPage.getContent();

        assertTrue(gotBooksResponses.containsAll(expectedBooksResponses));
    }

    @Test
    public void shouldCreateBook(){

        //given
        PublisherEntity publisher = new PublisherEntity("Publisher 123");

        publisher = publisherRepository.save(publisher);

        AuthorEntity author = AuthorEntity.builder()
            .firstname("Adam")
            .surname("Nowak")
            .books(new HashSet<>())
            .publishedBooksCount((short) 1)
            .mainPublisher(publisher)
            .build();

        author = authorRepository.save(author);

        String picture = "Picture 123";
        String encodedPicture = "UGljdHVyZSAxMjM=";

        CreateBookRequest createBookRequest = new CreateBookRequest(
            "Book 123",
            BigDecimal.valueOf(45.48),
            LocalDate.now(),
            encodedPicture, //base64 of "Picture123"
            publisher.getId().toString(),
            List.of(author.getId().toString())
        );

        String adminAccessToken = createAdminAndGetAccessToken("adam", "nowak");

        //when
        BookResponse gotBookResponse = RestAssured
        .given()
            .header(
                new Header(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer " + adminAccessToken
                )
            )
            .contentType(ContentType.JSON)
            .body(createBookRequest)
        .when()
            .post()
        .then()
            .statusCode(201)
            .extract()
            .as(BookResponse.class);

        //then
        assertNotNull(gotBookResponse);

        UUID gotBookId = UUID.fromString(gotBookResponse.id());
        Optional<BookEntity> createdBookOpt = bookRepository.findById(gotBookId);

        assertTrue(createdBookOpt.isPresent());

        BookEntity createdBook = createdBookOpt.get();

        assertEquals(createBookRequest.title(), createdBook.getTitle());
        assertEquals(createBookRequest.publicationDate(), createdBook.getPublicationDate());
        assertEquals(createBookRequest.price(), createdBook.getPrice());
        assertEquals(createBookRequest.publisherId(), createdBook.getPublisher().getId().toString());
        assertTrue(createdBook.getAuthors().contains(author));
        assertEquals(picture, new String(createdBook.getPicture()));

        assertEquals(createBookRequest.title(), gotBookResponse.title());
        assertEquals(createBookRequest.publicationDate(), gotBookResponse.publicationDate());
        assertEquals(createBookRequest.price(), gotBookResponse.price());
        assertEquals(createBookRequest.publisherId(), gotBookResponse.publisher().id());
        assertTrue(createBookRequest.authorsIds().contains(author.getId().toString()));
        assertEquals(encodedPicture, gotBookResponse.picture());
    }

    @Test
    public void shouldDeleteBook(){

        //given
        PublisherEntity publisher = new PublisherEntity("Publisher 123");

        publisher = publisherRepository.save(publisher);

        AuthorEntity author = AuthorEntity.builder()
            .firstname("Adam")
            .surname("Nowak")
            .publishedBooksCount((short) 1)
            .mainPublisher(null)
            .books(new HashSet<>())
            .build();

        author = authorRepository.save(author);

        BookEntity book = BookEntity.builder()
            .title("Book 123")
            .picture("Picture 123".getBytes(StandardCharsets.UTF_8))
            .publicationDate(LocalDate.now())
            .price(BigDecimal.valueOf(2.35))
            .publisher(publisher)
            .authors(List.of(author))
            .build();

        book = bookRepository.save(book);

        author.getBooks().add(book);

        String adminAccessToken = createAdminAndGetAccessToken("adam", "nowak");

        //when
        RestAssured
        .given()
            .header(
                new Header(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer " + adminAccessToken
                )
            )
        .when()
            .delete("/{bookId}", book.getId())
        .then()
            .statusCode(204);

        //then
    }
}
