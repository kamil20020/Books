package pl.books.magagement.controller.unit;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pl.books.magagement.config.CustomAccessDeniedHandler;
import pl.books.magagement.config.JwtFilter;
import pl.books.magagement.config.SecurityConfig;
import pl.books.magagement.controller.RoleController;
import pl.books.magagement.model.api.request.CreateRoleRequest;
import pl.books.magagement.model.entity.RoleEntity;
import pl.books.magagement.service.RoleService;
import pl.books.magagement.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoleController.class)
@Import(SecurityConfig.class)
class RoleControllerTest {

    private static final String API_URL_PREFIX = "/roles";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    private final ObjectMapper objectMapper;

    public RoleControllerTest(){

        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateRole() throws Exception {

        //given
        CreateRoleRequest createRoleRequest = new CreateRoleRequest("ADMIN");

        String createRoleRequestJson = objectMapper.writeValueAsString(createRoleRequest);

        //when
        MvcResult response = mockMvc.perform(
            post(API_URL_PREFIX)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(createRoleRequestJson)
        )
        .andDo(print())
        .andExpect(status().isCreated())
        .andReturn();

        String extractedBody = response.getResponse().getContentAsString();
        RoleEntity createdRole = objectMapper.readValue(extractedBody, RoleEntity.class);

        //then
        assertEquals("ADMIN", createdRole.getName());

        Mockito.verify(roleService).createRole("ADMIN");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotCreateDuplicateRole() throws Exception {

        //given
        CreateRoleRequest createRoleRequest = new CreateRoleRequest("ADMIN");

        String createRoleRequestJson = objectMapper.writeValueAsString(createRoleRequest);

        //when
        Mockito.doThrow(EntityExistsException.class).when(roleService).createRole(any());

        mockMvc.perform(
            post(API_URL_PREFIX)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(createRoleRequestJson)
        )
        .andDo(print())
        .andExpect(status().isConflict())
        .andReturn();

        //then
        Mockito.verify(roleService).createRole("ADMIN");
    }
}