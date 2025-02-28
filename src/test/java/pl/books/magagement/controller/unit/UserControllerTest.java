package pl.books.magagement.controller.unit;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.apiguardian.api.API;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pl.books.magagement.config.SecurityConfig;
import pl.books.magagement.model.api.request.RegisterRequest;
import pl.books.magagement.model.entity.RoleEntity;
import pl.books.magagement.model.mappers.UserMapper;
import pl.books.magagement.service.AuthorService;
import pl.books.magagement.service.RoleService;
import pl.books.magagement.service.UserService;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(SecurityConfig.class)
class UserControllerTest {

    private static final String API_URL_PREFIX = "/users";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    private final ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserControllerTest(){

        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @Test
    void shouldRegister() throws Exception {

        //given
        String username = "kamil";
        String password = "nowak";

        RegisterRequest registerRequest = new RegisterRequest(username, password);

        String registerRequestJson = objectMapper.writeValueAsString(registerRequest);

        //when
        mockMvc.perform(
            post(API_URL_PREFIX)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(registerRequestJson)
        )
        .andDo(print())
        .andExpect(status().isCreated());

        //then
        ArgumentCaptor<String> encodedPasswordCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(userService).register(eq(username), encodedPasswordCaptor.capture());

        String gotEncodedPassword = encodedPasswordCaptor.getValue();

        assertTrue(passwordEncoder.matches(password, gotEncodedPassword));
    }

    @Test
    void shouldNotRegisterWithDuplicateUsername() throws Exception {

        //given
        String username = "kamil";
        String password = "nowak";

        RegisterRequest registerRequest = new RegisterRequest(username, password);

        String registerRequestJson = objectMapper.writeValueAsString(registerRequest);

        //when
        Mockito.when(userService.register(anyString(), anyString())).thenThrow(EntityExistsException.class);

        mockMvc.perform(
            post(API_URL_PREFIX)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(registerRequestJson)
        )
        .andDo(print())
        .andExpect(status().isConflict());


        ArgumentCaptor<String> encodedPasswordCaptor = ArgumentCaptor.forClass(String.class);

        //then
        Mockito.verify(userService).register(eq(username), encodedPasswordCaptor.capture());

        String gotEncodedPassword = encodedPasswordCaptor.getValue();

        assertTrue(passwordEncoder.matches(password, gotEncodedPassword));
    }

    @Test
    public void shouldGetUserRoles() throws Exception {

        //given
        UUID userId = UUID.randomUUID();

        UUID roleId = UUID.randomUUID();

        RoleEntity role = new RoleEntity(roleId, "admin");

        Set<RoleEntity> expectedRoles = Set.of(role);

        //when
        Mockito.when(roleService.getUserRoles(any())).thenReturn(expectedRoles);

        MvcResult mvcResult = mockMvc.perform(
            get(API_URL_PREFIX + "/" + userId + "/roles")
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

        String gotResponse = mvcResult.getResponse().getContentAsString();

        Set<RoleEntity> gotRoles = objectMapper.readValue(gotResponse, new TypeReference<Set<RoleEntity>>(){});

        //then
        assertEquals(1, gotRoles.size());
        assertEquals(roleId, gotRoles.iterator().next().getId());

        Mockito.verify(roleService).getUserRoles(userId);
    }

    @Test
    public void shouldNotGetRolesWhenUserIsNotFound() throws Exception {

        //given
        UUID userId = UUID.randomUUID();

        //when
        Mockito.when(roleService.getUserRoles(any())).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(
            get(API_URL_PREFIX + "/" + userId + "/roles")
        )
        .andDo(print())
        .andExpect(status().isNotFound())
        .andReturn();

        //then
        Mockito.verify(roleService).getUserRoles(userId);
    }

    @Test
    public void shouldGrantRole() throws Exception {

        //given
        UUID userId = UUID.randomUUID();

        UUID roleId = UUID.randomUUID();

        //when
        mockMvc.perform(
            post(API_URL_PREFIX + "/" + userId + "/roles/" + roleId)
        )
        .andDo(print())
        .andExpect(status().isCreated())
        .andReturn();

        //then
        Mockito.verify(roleService).grantRole(userId, roleId);
    }

    @Test
    public void shouldNotGrantDuplicateRole() throws Exception {

        //given
        UUID userId = UUID.randomUUID();

        UUID roleId = UUID.randomUUID();

        //when
        Mockito.doThrow(EntityExistsException.class).when(roleService).grantRole(any(), any());

        mockMvc.perform(
            post(API_URL_PREFIX + "/" + userId + "/roles/" + roleId)
        )
        .andDo(print())
        .andExpect(status().isConflict())
        .andReturn();

        //then
        Mockito.verify(roleService).grantRole(userId, roleId);
    }

    @Test
    public void shouldRevokeRole() throws Exception {

        //given
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        //when
        mockMvc.perform(
            delete(API_URL_PREFIX + "/" + userId + "/roles/" + roleId)
        )
        .andDo(print())
        .andExpect(status().isNoContent());

        //then
        Mockito.verify(roleService).revokeRole(userId, roleId);
    }

    @Test
    public void shouldNotRevokeNotFoundRole() throws Exception {

        //given
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        //when
        Mockito.doThrow(EntityNotFoundException.class).when(roleService).revokeRole(any(), any());

        mockMvc.perform(
            delete(API_URL_PREFIX + "/" + userId + "/roles/" + roleId)
        )
        .andDo(print())
        .andExpect(status().isNotFound());

        //then
        Mockito.verify(roleService).revokeRole(userId, roleId);
    }
}