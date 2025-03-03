package pl.books.magagement.security;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.books.magagement.config.SecurityConfig;
import pl.books.magagement.controller.SecurityController;
import pl.books.magagement.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SecurityController.class)
@Import(SecurityConfig.class)
class SecurityControllerTest {

    private final String API_URL_PREFIX = "/security";
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testPublic() throws Exception {

        //when
        MvcResult mvcResult = mockMvc.perform(
            get(API_URL_PREFIX + "/public")
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        String gotContent = mvcResult.getResponse().getContentAsString();

        //then
        assertEquals("public", gotContent);
    }

    @Test
    @WithMockUser(username = "kamil", password = "nowak")
    void onlyLoggedUserShouldAccess() throws Exception {

        //given
        String basicAuthHeaderContent = "Basic a2FtaWw6bm93YWs="; //base64 of kamil.nowak

        //when
        MvcResult mvcResult = mockMvc.perform(
            get(API_URL_PREFIX + "/requires-login")
            .header(HttpHeaders.AUTHORIZATION, basicAuthHeaderContent)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        String gotContent = mvcResult.getResponse().getContentAsString();

        //then

        assertEquals("login", gotContent);
    }

    @Test
    void notLoggedUserShouldNotAccess() throws Exception {

        //when
        MvcResult mvcResult = mockMvc.perform(
            get(API_URL_PREFIX + "/requires-login")
        )
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andReturn();

        //then
    }

    @Test
    @WithMockUser(username = "kamil", password = "nowak", roles = "ADMIN")
    void onlyAdminShouldAccess() throws Exception {

        //given
        String basicAuthHeaderContent = "Basic a2FtaWw6bm93YWs="; //base64 of kamil.nowak

        //when
        MvcResult mvcResult = mockMvc.perform(
            get(API_URL_PREFIX + "/requires-admin")
            .header(HttpHeaders.AUTHORIZATION, basicAuthHeaderContent)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        String gotContent = mvcResult.getResponse().getContentAsString();

        //then
        assertEquals("admin", gotContent);
    }

    @Test
    @WithMockUser(username = "kamil", password = "nowak")
    void regularUserShouldNotAccess() throws Exception {

        //given
        String basicAuthHeaderContent = "Basic a2FtaWw6bm93YWs="; //base64 of kamil.nowak

        //when
        MvcResult mvcResult = mockMvc.perform(
            get(API_URL_PREFIX + "/requires-admin")
            .header(HttpHeaders.AUTHORIZATION, basicAuthHeaderContent)
        )
            .andDo(print())
            .andExpect(status().isForbidden())
            .andReturn();

        //then
    }
}