package pl.books.magagement.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.books.magagement.model.entity.UserEntity;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.0");

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        userRepository.deleteAll();
    }

    @ParameterizedTest
    @CsvSource(value = {
        "kamil",
        "Kamil",
        "kamiL",
        "kaMil",
        "KAMIL",
        "kaMIl"
    })
    void shouldFindByUsername() {

        //given
        String checkUsername = "kamil.nowak";

        UserEntity expectedUser = UserEntity.builder()
            .username("kamil.nowak")
            .password("Pass")
        .build();

        expectedUser = userRepository.save(expectedUser);

        //when
        Optional<UserEntity> gotUserOpt = userRepository.findByUsernameIgnoreCase(checkUsername);

        //then
        assertTrue(gotUserOpt.isPresent());
        assertEquals(expectedUser, gotUserOpt.get());
    }

    @ParameterizedTest
    @CsvSource(value = {
        "adam.kowalski",
        "kamil.kowalski",
        "adam.nowak"
    })
    void shouldNotFindByUsernameWhenUsernameDoesNotExists(String checkUsername){

        //given
        UserEntity savedUser = UserEntity.builder()
            .username("kamil.nowak")
            .password("Pass")
            .build();

        userRepository.save(savedUser);

        //when
        Optional<UserEntity> gotUserOpt = userRepository.findByUsernameIgnoreCase(checkUsername);

        //then
        assertTrue(gotUserOpt.isEmpty());
    }

    @Test
    void shouldGetTrueWhenExistsByUsername() {

        //given
        UserEntity user = UserEntity.builder()
            .username("kamil")
            .password("nowak")
            .roles(new HashSet<>())
            .build();

        user = userRepository.save(user);

        //when

        boolean actual = userRepository.existsByUsernameIgnoreCase("kamil");

        //then
        assertTrue(actual);
    }

    @Test
    void shouldGetFalseWhenDoestNotExistsByUsername() {

        //given
        UserEntity user = UserEntity.builder()
            .username("kamil")
            .password("nowak")
            .roles(new HashSet<>())
            .build();

        //when
        boolean actual = userRepository.existsByUsernameIgnoreCase("adam");

        //then
        assertFalse(actual);
    }
}