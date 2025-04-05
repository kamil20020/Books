package pl.books.magagement.repository;

import org.junit.jupiter.api.BeforeAll;
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
import pl.books.magagement.model.entity.PublisherEntity;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PublisherRepositoryTest {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");

    @Autowired
    private PublisherRepository publisherRepository;

    @ParameterizedTest
    @CsvSource(value = {
        "Publisher 123, true",
        "publisher 123, true",
        "publisheR 123, true",
        "pubLisher 123, true",
        "Publisher 12, false",
        "Publisher 1234, false",
        "1Publisher 123, false",
        "Publ2isher 123, false",
    })
    void shouldGetExistsByNameIgnoreCase(String name, boolean doesExist) {

        //given
        PublisherEntity publisher = new PublisherEntity("Publisher 123");
        publisherRepository.save(publisher);

        //when
        boolean gotResult = publisherRepository.existsByNameIgnoreCase(name);

        //then
        assertEquals(doesExist, gotResult);
    }
}