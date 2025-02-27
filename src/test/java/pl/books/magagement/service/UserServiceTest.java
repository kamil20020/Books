package pl.books.magagement.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.books.magagement.model.entity.RoleEntity;
import pl.books.magagement.model.entity.UserEntity;
import pl.books.magagement.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldLoadUserByUsername() {

        //given
        String checkUsername = "kamil.nowak";

        UserEntity expectedUser = UserEntity.builder()
            .id(UUID.randomUUID())
            .username("kamil.nowak")
            .password("Pass")
        .build();

        Optional<UserEntity> expectedUserOpt = Optional.of(expectedUser);

        //when
        Mockito.when(userRepository.findByUsernameIgnoreCase(anyString())).thenReturn(expectedUserOpt);

        UserEntity gotUser = (UserEntity) userService.loadUserByUsername(checkUsername);

        //then
        assertNotNull(gotUser);
        assertEquals(expectedUser, gotUser);

        Mockito.verify(userRepository).findByUsernameIgnoreCase(checkUsername);
    }

    @Test
    void shouldNotLoadUserByUsernameWhenUsernameDoesNotExist(){

        //given
        String checkUsername = "kamil.nowak";

        //when
        Mockito.when(userRepository.findByUsernameIgnoreCase(anyString())).thenThrow(EntityNotFoundException.class);

        //then
        assertThrows(
            EntityNotFoundException.class,
            () -> userService.loadUserByUsername(checkUsername)
        );

        Mockito.verify(userRepository).findByUsernameIgnoreCase(checkUsername);
    }

    @Test
    void shouldRegister(){

        //given
        String username = "kamil";
        String password = "bm93YWs=";

        //when
        Mockito.when(userRepository.existsByUsernameIgnoreCase(anyString())).thenReturn(false);
        Mockito.when(userRepository.save(any())).thenReturn(new UserEntity());

        UserEntity createdUser = userService.register(username, password);

        //then
        ArgumentCaptor<UserEntity> toCreateUserCaptor = ArgumentCaptor.forClass(UserEntity.class);

        assertNotNull(createdUser);

        Mockito.verify(userRepository).existsByUsernameIgnoreCase(username);
        Mockito.verify(userRepository).save(toCreateUserCaptor.capture());

        UserEntity toCreateUser = toCreateUserCaptor.getValue();

        assertEquals(username, toCreateUser.getUsername());
        assertEquals(password, toCreateUser.getPassword());
    }

    @Test
    void shouldNotRegisterUserWithDuplicateUsername(){

        //given
        String duplicateUsername = "kamil";
        String password = "bm93YWs=";

        //when
        Mockito.when(userRepository.existsByUsernameIgnoreCase(anyString())).thenReturn(true);

        //then
        assertThrows(
            EntityExistsException.class,
            () -> userService.register(duplicateUsername, password)
        );

        Mockito.verify(userRepository).existsByUsernameIgnoreCase(duplicateUsername);
    }

    @Test
    void shouldGetById(){

        //given
        UUID userId = UUID.randomUUID();

        UserEntity expectedUser = UserEntity.builder()
            .id(userId)
            .username("kamil")
            .password("nowak")
            .build();

        Optional<UserEntity> expectedUserOpt = Optional.of(expectedUser);

        //when
        Mockito.when(userRepository.findById(any())).thenReturn(expectedUserOpt);

        UserEntity gotUser = userService.getById(userId);

        //then
        assertEquals(expectedUser, gotUser);

        Mockito.verify(userRepository).findById(userId);
    }

    @Test
    void shouldNotGetUserByNotFoundId(){

        //given
        UUID userId = UUID.randomUUID();

        //when
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.empty());

        //then
        assertThrows(
            EntityNotFoundException.class,
            () -> userService.getById(userId)
        );

        Mockito.verify(userRepository).findById(userId);
    }
}