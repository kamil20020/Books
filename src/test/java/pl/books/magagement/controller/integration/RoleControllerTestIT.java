package pl.books.magagement.controller.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import pl.books.magagement.model.api.request.CreateRoleRequest;
import pl.books.magagement.model.entity.RoleEntity;
import pl.books.magagement.model.entity.UserEntity;
import pl.books.magagement.repository.RoleRepository;
import pl.books.magagement.repository.UserRepository;
import pl.books.magagement.service.RoleService;
import pl.books.magagement.service.UserService;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleControllerTestIT {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSqlContainer = new PostgreSQLContainer<>("postgres:13");

    @LocalServerPort
    public Integer port;

    @Autowired
    public RoleRepository roleRepository;

    @Autowired
    public UserService userService;

    @Autowired
    public RoleService roleService;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp(){

        RestAssured.baseURI = "http://localhost/roles";
        RestAssured.port = port;

        roleRepository.deleteAll();
        userRepository.deleteAll();
    }

    public void createAdmin(String username, String password){

        String encodedPassword = passwordEncoder.encode(password);

        UserEntity newUser = UserEntity.builder()
                .username(username)
                .password(encodedPassword)
                .roles(new HashSet<>())
                .build();

        UserEntity user = userRepository.save(newUser);

        RoleEntity adminRole = new RoleEntity("ADMIN");

        adminRole = roleRepository.save(adminRole);

        roleService.grantRole(user.getId(), adminRole.getId());
    }

    @Test
    void shouldCreateRole() {

        //given
        createAdmin("adam_nowak", "nowak");

        CreateRoleRequest createRoleRequest = new CreateRoleRequest("WRITER");

        //when
        RestAssured
        .given()
            .contentType(ContentType.JSON)
            .body(createRoleRequest)
            .auth()
                .preemptive()
                .basic(
                    "adam_nowak",
                    "nowak"
                )
        .when()
            .post()
        .then()
            .statusCode(201);

        //then
        assertEquals(2, roleRepository.count());
        assertTrue(roleRepository.existsByNameIgnoreCase("WRITER"));
    }

    @Test
    void shouldNotCreateDuplicateRole() {

        //given
        createAdmin("adam_nowak", "nowak");

        CreateRoleRequest createRoleRequest = new CreateRoleRequest("ADMIN");

        //when
        RestAssured
        .given()
            .contentType(ContentType.JSON)
            .body(createRoleRequest)
            .auth()
                .preemptive()
                .basic(
                    "adam_nowak",
                    "nowak"
                )
        .when()
            .post()
        .then()
            .statusCode(409);

        //then
        assertEquals(1, roleRepository.count());
    }

    @Test
    public void shouldDeleteById(){

        //given
        createAdmin("adam", "nowak");

        RoleEntity role = roleRepository.findAll().get(0);

        String accessToken = userService.login("adam", "nowak").accessToken();

        //when
        RestAssured
            .given()
                .header(new Header(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer " + accessToken
                ))
            .when()
                .delete("/{roleId}", role.getId())
            .then()
                .statusCode(204);

        //then
        assertEquals(0, roleRepository.count());
    }
}