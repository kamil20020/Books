package pl.books.magagement.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.books.magagement.model.entity.RoleEntity;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleRepositoryTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContrainer = new PostgreSQLContainer<>("postgres:13");

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp(){
        roleRepository.deleteAll();
    }

    @Test
    void shouldGetTrueIfExistsByNameIgnoreCase() {

        //given
        RoleEntity expectedRole = new RoleEntity("admin");
        roleRepository.save(expectedRole);

        //when

        //then
        assertTrue(roleRepository.existsByNameIgnoreCase("admin"));
        assertTrue(roleRepository.existsByNameIgnoreCase("Admin"));
        assertTrue(roleRepository.existsByNameIgnoreCase("admiN"));
        assertTrue(roleRepository.existsByNameIgnoreCase("aDMin"));
        assertTrue(roleRepository.existsByNameIgnoreCase("ADMIN"));
    }

    @Test
    void shouldGetFalseIfNotExistsByNameIgnoreCase(){

        //given
        RoleEntity expectedRole = new RoleEntity("admin");
        roleRepository.save(expectedRole);

        //when

        //then
        assertFalse(roleRepository.existsByNameIgnoreCase("1admin"));
        assertFalse(roleRepository.existsByNameIgnoreCase("admin2"));
        assertFalse(roleRepository.existsByNameIgnoreCase("1admin2"));
        assertFalse(roleRepository.existsByNameIgnoreCase("writer"));
    }
}