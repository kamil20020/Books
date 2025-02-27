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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pl.books.magagement.model.api.request.PatchUserRequest;
import pl.books.magagement.model.api.request.RegisterRequest;
import pl.books.magagement.model.api.response.UserResponse;
import pl.books.magagement.model.entity.RoleEntity;
import pl.books.magagement.model.entity.UserEntity;
import pl.books.magagement.repository.RoleRepository;
import pl.books.magagement.repository.UserRepository;
import pl.books.magagement.service.RoleService;
import pl.books.magagement.service.UserService;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserControllerTestIT {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");

    @LocalServerPort
    private Integer port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp(){

        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost/users";

        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void shouldRegister() {

        //given
        String expectedUsername = "kamil";
        String expectedRawPassword = "nowak";

        RegisterRequest registerRequest = new RegisterRequest(expectedUsername, expectedRawPassword);

        //when
        RestAssured
        .given()
            .contentType(ContentType.JSON)
            .body(registerRequest)
        .when()
            .post()
        .then()
            .statusCode(201);

        UserEntity createdUser = userRepository.findAll().iterator().next();

        //then
        assertNotNull(createdUser);
        assertEquals(1, userRepository.count());
        assertEquals(expectedUsername, createdUser.getUsername());
        assertTrue(passwordEncoder.matches(expectedRawPassword, createdUser.getPassword()));
    }

    @Test
    void shouldNotRegisterDuplicateUsername(){

        //given
        String expectedUsername = "kamil";
        String expectedRawPassword = "nowak";

        UserEntity user = UserEntity.builder()
            .username(expectedUsername)
            .password(expectedRawPassword)
            .roles(new HashSet<>())
            .build();

        userRepository.save(user);

        RegisterRequest registerRequest = new RegisterRequest(expectedUsername, expectedRawPassword);

        //when
        RestAssured
        .given()
            .contentType(ContentType.JSON)
            .body(registerRequest)
        .when()
            .post()
        .then()
            .statusCode(409);

        //then
        assertEquals(1, userRepository.count());
    }

    @Test
    public void shouldGetRoles(){

        //given
        UserEntity user = UserEntity.builder()
            .username("kamil")
            .password("nowak")
            .roles(new HashSet<>())
            .build();

        user = userRepository.save(user);

        RoleEntity adminRole = new RoleEntity("admin");
        RoleEntity writerRole = new RoleEntity("writer");

        adminRole = roleRepository.save(adminRole);
        writerRole = roleRepository.save(writerRole);

        Set<RoleEntity> expectedRoles = Set.of(adminRole, writerRole);

        roleService.grantRole(user.getId(), adminRole.getId());
        roleService.grantRole(user.getId(), writerRole.getId());

        //when
        Set<RoleEntity> gotRoles = RestAssured
        .given()
        .when()
            .get("/{userId}/roles", user.getId())
        .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<Set<RoleEntity>>(){});

        //then
        assertEquals(2, gotRoles.size());
        assertEquals(expectedRoles, gotRoles);
    }

    @Test
    public void shouldGrantRole(){

        //given
        RoleEntity role = new RoleEntity("admin");

        role = roleRepository.save(role);

        UserEntity user = UserEntity.builder()
            .username("adam")
            .password("nowak")
            .roles(new HashSet<>())
            .build();

        user = userRepository.save(user);

        //when
        RestAssured
        .given()
            .pathParam("userId", user.getId())
            .pathParam("roleId", role.getId())
        .when()
            .post("/{userId}/roles/{roleId}")
        .then()
            .statusCode(201);

        user = userRepository.findById(user.getId()).get();

        //then
        assertEquals(1, user.getRoles().size());
        assertEquals(role, user.getRoles().iterator().next());
    }

    @Test
    public void shouldRevokeRole(){

        //given
        RoleEntity role = new RoleEntity("admin");

        role = roleRepository.save(role);

        UserEntity user = UserEntity.builder()
            .username("kamil")
            .password("nowak")
            .roles(new HashSet<>())
            .build();

        user = userRepository.save(user);

        roleService.grantRole(user.getId(), role.getId());

        //when
        RestAssured
        .given()
            .pathParam("userId", user.getId())
            .pathParam("roleId", role.getId())
        .when()
            .delete("/{userId}/roles/{roleId}")
        .then()
            .statusCode(204);

        user = userRepository.save(user);

        //then
        assertTrue(user.getRoles().isEmpty());
    }

    @Test
    public void shouldPatchUserById(){

        //given
        RoleEntity adminRole = new RoleEntity("ADMIN");

        adminRole = roleRepository.save(adminRole);

        UserEntity user = UserEntity.builder()
            .username("adam_nowak")
            .password("$2a$10$y/VWKoTjVR9jTiCtjQB8XuWpj2TAdH3.IXXxaE0LZKF9cC1WY8erO")
            .roles(Set.of(adminRole))
            .build();

        user = userRepository.save(user);

        PatchUserRequest request = new PatchUserRequest("adam.nowak");

        //when
        UserResponse userResponse = RestAssured
        .given()
            .contentType(ContentType.JSON)
            .body(request)
            .auth()
                .preemptive()
                .basic(
                    "adam_nowak",
                    "nowak"
                )
        .when()
            .patch("/{userId}", user.getId())
        .then()
            .statusCode(200)
            .extract()
            .as(UserResponse.class);

        //then
        user = userRepository.findById(user.getId()).get();

        assertEquals(request.username(), userResponse.username());
        assertEquals(request.username(), user.getUsername());
    }

    @Test
    public void shouldDeleteUserById(){

        //given
        RoleEntity adminRole = new RoleEntity("ADMIN");

        adminRole = roleRepository.save(adminRole);

        UserEntity user = UserEntity.builder()
            .username("kamil_nowak")
            .password("$2a$10$y/VWKoTjVR9jTiCtjQB8XuWpj2TAdH3.IXXxaE0LZKF9cC1WY8erO")
            .roles(Set.of(adminRole))
            .build();

        user = userRepository.save(user);

        //when
        RestAssured
        .given()
            .pathParam("userId", user.getId())
            .auth()
                .preemptive()
                .basic(
                    "kamil_nowak",
                    "nowak"
                )
        .when()
            .delete("/{userId}")
        .then()
            .statusCode(204);

        //then
        assertEquals(0, userRepository.count());
    }
}