package pl.books.magagement.controller.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.books.magagement.model.api.request.CreateRoleRequest;
import pl.books.magagement.model.entity.RoleEntity;
import pl.books.magagement.repository.RoleRepository;
import pl.books.magagement.repository.UserRepository;

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
    public UserRepository userRepository;

    @BeforeEach
    public void setUp(){

        RestAssured.baseURI = "http://localhost/roles";
        RestAssured.port = port;

        roleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateRole() {

        //given
        CreateRoleRequest createRoleRequest = new CreateRoleRequest("admin");

        //when
        RestAssured
        .given()
            .contentType(ContentType.JSON)
            .body(createRoleRequest)
        .when()
            .post()
        .then()
            .statusCode(201);

        //then
        assertEquals(1, roleRepository.count());
        assertEquals("admin", roleRepository.findAll().iterator().next().getName());
    }

    @Test
    void shouldNotCreateDuplicateRole() {

        //given
        RoleEntity role = new RoleEntity("admin");

        role = roleRepository.save(role);

        CreateRoleRequest createRoleRequest = new CreateRoleRequest("admin");

        //when
        RestAssured
        .given()
            .contentType(ContentType.JSON)
            .body(createRoleRequest)
        .when()
            .post()
        .then()
            .statusCode(409);

        //then
        assertEquals(1, roleRepository.count());
        assertEquals(role, roleRepository.findAll().iterator().next());
    }
}