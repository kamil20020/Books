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
import pl.books.magagement.repository.RoleRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private RoleService roleService;

    @Test
    void shouldGetById() {

        //given
        UUID roleId = UUID.randomUUID();

        RoleEntity expectedRole = new RoleEntity(roleId,"admin");

        Optional<RoleEntity> expectedRoleOpt = Optional.of(expectedRole);
        
        //when
        Mockito.when(roleRepository.findById(any())).thenReturn(expectedRoleOpt);

        RoleEntity gotRole = roleService.getById(roleId);

        //then
        assertEquals(expectedRole, gotRole);

        Mockito.verify(roleRepository).findById(roleId);
    }

    @Test
    void shouldNotGetByNotFoundId(){

        //given
        UUID roleId = UUID.randomUUID();

        //when
        Mockito.when(roleRepository.findById(any())).thenReturn(Optional.empty());

        //then
        assertThrows(
            EntityNotFoundException.class,
            () -> roleService.getById(roleId)
        );

        Mockito.verify(roleRepository).findById(roleId);
    }

    @Test
    void shouldGetFewUserRoles() {

        //given
        Set<RoleEntity> expectedRoles = Set.of(
            new RoleEntity("admin"),
            new RoleEntity("writer")
        );

        UUID userId = UUID.randomUUID();

        UserEntity expectedUser = UserEntity.builder()
            .roles(expectedRoles)
            .build();

        //when
        Mockito.when(userService.getById(any())).thenReturn(expectedUser);

        Set<RoleEntity> gotRoles = roleService.getUserRoles(userId);

        //then
        assertNotNull(gotRoles);
        assertEquals(2, gotRoles.size());
        assertEquals(expectedRoles, gotRoles);

        Mockito.verify(userService).getById(userId);
    }

    @Test
    void shouldGetNoUserRoles() {

        //given
        Set<RoleEntity> expectedRoles = new HashSet<>();

        UUID userId = UUID.randomUUID();

        UserEntity user = UserEntity.builder()
            .roles(expectedRoles)
            .build();

        //when
        Mockito.when(userService.getById(any())).thenReturn(user);

        Set<RoleEntity> gotRoles = roleService.getUserRoles(userId);

        //then
        assertNotNull(gotRoles);
        assertEquals(expectedRoles, gotRoles);

        Mockito.verify(userService).getById(userId);
    }

    @Test
    void shouldNotGetUserRolesWhenUserWasNotFound() {

        //given
        UUID userId = UUID.randomUUID();

        //when
        Mockito.when(userService.getById(any())).thenThrow(EntityNotFoundException.class);

        //then
        assertThrows(
            EntityNotFoundException.class,
            () -> roleService.getUserRoles(userId)
        );

        Mockito.verify(userService).getById(userId);
    }

    @Test
    void shouldCreateRole() {

        //given
        String roleName = "admin";

        //when
        Mockito.when(roleRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);

        roleService.createRole(roleName);

        //then
        ArgumentCaptor<RoleEntity> newRoleCaptor = ArgumentCaptor.forClass(RoleEntity.class);

        Mockito.verify(roleRepository).save(newRoleCaptor.capture());
        Mockito.verify(roleRepository).existsByNameIgnoreCase(roleName);

        RoleEntity gotRole = newRoleCaptor.getValue();

        assertEquals(roleName, gotRole.getName());
    }

    @Test
    void shouldNotCreateDuplicateRole() {

        //given
        String roleName = "admin";

        //when
        Mockito.when(roleRepository.existsByNameIgnoreCase(anyString())).thenReturn(true);

        //then
        assertThrows(
            EntityExistsException.class,
            () -> roleService.createRole(roleName)
        );

        Mockito.verify(roleRepository).existsByNameIgnoreCase(roleName);
    }

    @Test
    void shouldGrantRole() {

        //given
        UUID userId = UUID.randomUUID();

        UserEntity user = UserEntity.builder()
            .roles(new HashSet<>())
            .build();

        UUID roleId = UUID.randomUUID();

        RoleEntity role = new RoleEntity("admin");
        Optional<RoleEntity> roleOpt = Optional.of(role);

        //when
        Mockito.when(userService.getById(any())).thenReturn(user);
        Mockito.when(roleRepository.findById(any())).thenReturn(roleOpt);

        roleService.grantRole(userId, roleId);

        //then
        assertNotNull(user.getRoles());
        assertEquals(1, user.getRoles().size());
        assertEquals(role, user.getRoles().iterator().next());

        Mockito.verify(userService).getById(userId);
        Mockito.verify(roleRepository).findById(roleId);
    }

    @Test
    void shouldNotGrantRoleWhenUserIsNotFound() {

        //given
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        //when
        Mockito.when(userService.getById(any())).thenThrow(EntityNotFoundException.class);

        //then
        assertThrows(
            EntityNotFoundException.class,
            () -> roleService.grantRole(userId, roleId)
        );

        Mockito.verify(userService).getById(userId);
    }

    @Test
    void shouldNotGrantRoleWhenRoleIsNotFound() {

        //given
        UUID userId = UUID.randomUUID();

        UserEntity user = UserEntity.builder()
            .roles(new HashSet<>())
            .build();

        UUID roleId = UUID.randomUUID();

        //when
        Mockito.when(userService.getById(any())).thenReturn(user);
        Mockito.when(roleRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(
            EntityNotFoundException.class,
            () -> roleService.grantRole(userId, roleId)
        );

        //then

        Mockito.verify(userService).getById(userId);
        Mockito.verify(roleRepository).findById(roleId);
    }

    @Test
    void shouldNotGrantDuplicateRole() {

        //given
        UUID userId = UUID.randomUUID();

        UserEntity user = UserEntity.builder()
                .roles(new HashSet<>())
                .build();

        UUID roleId = UUID.randomUUID();

        RoleEntity role = new RoleEntity("admin");
        Optional<RoleEntity> roleOpt = Optional.of(role);

        user.getRoles().add(role);

        //when
        Mockito.when(userService.getById(any())).thenReturn(user);
        Mockito.when(roleRepository.findById(any())).thenReturn(roleOpt);

        assertThrows(
            EntityExistsException.class,
            () -> roleService.grantRole(userId, roleId)
        );

        //then

        Mockito.verify(userService).getById(userId);
        Mockito.verify(roleRepository).findById(roleId);
    }

    @Test
    void shouldRevokeRole() {

        //given
        UUID roleId = UUID.randomUUID();

        RoleEntity role = new RoleEntity("admin");

        Optional<RoleEntity> roleOpt = Optional.of(role);

        Set<RoleEntity> expectedRoles = new HashSet<>();
        expectedRoles.add(role);

        UUID userId = UUID.randomUUID();

        UserEntity user = UserEntity.builder()
            .roles(expectedRoles)
            .build();

        //when
        Mockito.when(userService.getById(any())).thenReturn(user);
        Mockito.when(roleRepository.findById(any())).thenReturn(roleOpt);

        roleService.revokeRole(userId, roleId);

        //then
        assertNotNull(user.getRoles());
        assertEquals(0, user.getRoles().size());

        Mockito.verify(userService).getById(userId);
        Mockito.verify(roleRepository).findById(roleId);
    }

    @Test
    void shouldNotRevokeNotGrantedRole() {

        //given
        UUID userId = UUID.randomUUID();

        UserEntity user = UserEntity.builder()
            .roles(new HashSet<>())
            .build();

        UUID roleId = UUID.randomUUID();

        RoleEntity role = new RoleEntity("admin");

        Optional<RoleEntity> roleOpt = Optional.of(role);

        //when
        Mockito.when(userService.getById(any())).thenReturn(user);
        Mockito.when(roleRepository.findById(any())).thenReturn(roleOpt);

        //then
        assertThrows(
            EntityNotFoundException.class,
            () -> roleService.revokeRole(userId, roleId)
        );

        Mockito.verify(userService).getById(userId);
        Mockito.verify(roleRepository).findById(roleId);
    }
}