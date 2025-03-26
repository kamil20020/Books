package pl.books.magagement.controller.integration;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pl.books.magagement.config.JwtFilter;
import pl.books.magagement.config.JwtService;
import pl.books.magagement.model.api.request.LoginRequest;
import pl.books.magagement.model.api.request.PatchUserRequest;
import pl.books.magagement.model.api.request.RegisterRequest;
import pl.books.magagement.model.api.response.LoginResponse;
import pl.books.magagement.model.api.response.UserResponse;
import pl.books.magagement.model.entity.RoleEntity;
import pl.books.magagement.model.entity.UserEntity;
import pl.books.magagement.repository.RoleRepository;
import pl.books.magagement.repository.UserRepository;
import pl.books.magagement.service.RoleService;
import pl.books.magagement.service.UserService;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp(){

        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost/users";

        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    public UserEntity createAdmin(String username, String password){

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

        return user;
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
        UserEntity admin = createAdmin("kamil", "nowak");

        RoleEntity writerRole = new RoleEntity("WRITER");

        writerRole = roleRepository.save(writerRole);

        roleService.grantRole(admin.getId(), writerRole.getId());

        //when
        Set<RoleEntity> gotRoles = RestAssured
        .given()
            .auth()
                .preemptive()
                .basic(
                    "kamil",
                    "nowak"
                )
        .when()
            .get("/{userId}/roles", admin.getId())
        .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<Set<RoleEntity>>(){});

        //then
        assertEquals(2, gotRoles.size());

        boolean grantedRolesExist = gotRoles.stream()
            .map(role -> role.getName())
            .collect(Collectors.toList())
            .containsAll(List.of("ADMIN", "WRITER"));

        assertTrue(grantedRolesExist);
    }

    @Test
    public void shouldLogin(){

        //given
        createAdmin("kamil", "nowak");

        LoginRequest request = new LoginRequest("kamil", "nowak");

        //when
        LoginResponse response = RestAssured
        .given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/login")
        .then()
            .statusCode(200)
            .extract()
            .as(LoginResponse.class);

        String accessToken = response.accessToken();

        //then
        assertDoesNotThrow(() -> jwtService.verifyToken(accessToken));
        assertEquals("kamil", jwtService.getUsername(accessToken));
    }

    @Test
    public void shouldGrantRole(){

        //given
        RoleEntity role = new RoleEntity("WRITER");
        role = roleRepository.save(role);

        UserEntity user = createAdmin("adam_nowak", "nowak");

        //when
        RestAssured
        .given()
            .pathParam("userId", user.getId())
            .pathParam("roleId", role.getId())
            .auth()
                .preemptive()
                .basic(
                        "adam_nowak",
                        "nowak"
                )
        .when()
            .post("/{userId}/roles/{roleId}")
        .then()
            .statusCode(201);

        user = userRepository.findById(user.getId()).get();

        //then
        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().contains(role));
    }

    @Test
    public void shouldRevokeRole(){

        //given
        UserEntity user = createAdmin("adam_nowak", "nowak");
        userRepository.flush();

        RoleEntity adminRole = roleRepository.findAll().get(0);

        //when
        RestAssured
        .given()
            .pathParam("userId", user.getId())
            .pathParam("roleId", adminRole.getId())
            .auth()
                .preemptive()
                .basic(
                    "adam_nowak",
                    "nowak"
                )
        .when()
            .delete("/{userId}/roles/{roleId}")
        .then()
            .statusCode(204);

        user = userRepository.save(user);

        //then
        assertEquals(0, user.getRoles().size());
    }

    @Test
    public void shouldPatchUserById(){

        //given
        UserEntity user = createAdmin("adam_nowak", "nowak"); //$2a$10$y/VWKoTjVR9jTiCtjQB8XuWpj2TAdH3.IXXxaE0LZKF9cC1WY8erO"

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
        UserEntity user = createAdmin("adam_nowak", "nowak"); //$2a$10$y/VWKoTjVR9jTiCtjQB8XuWpj2TAdH3.IXXxaE0LZKF9cC1WY8erO

        //when
        RestAssured
        .given()
            .pathParam("userId", user.getId())
            .auth()
                .preemptive()
                .basic(
                    "adam_nowak",
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