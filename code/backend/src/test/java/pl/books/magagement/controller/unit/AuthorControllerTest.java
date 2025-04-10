package pl.books.magagement.controller.unit;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.books.magagement.config.CustomAccessDeniedHandler;
import pl.books.magagement.config.JwtFilter;
import pl.books.magagement.config.SecurityConfig;
import pl.books.magagement.controller.AuthorController;
import pl.books.magagement.model.api.request.CreateAuthorRequest;
import pl.books.magagement.model.api.response.AuthorHeader;
import pl.books.magagement.model.entity.AuthorEntity;
import pl.books.magagement.model.entity.PublisherEntity;
import pl.books.magagement.model.internal.CreateAuthor;
import pl.books.magagement.model.mappers.AuthorMapper;
import pl.books.magagement.model.mappers.AuthorMapperImpl;
import pl.books.magagement.model.mappers.PublisherMapperImpl;
import pl.books.magagement.service.AuthorService;
import pl.books.magagement.service.UserService;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthorController.class)
@Import(SecurityConfig.class)
class AuthorControllerTest {

    private static final String API_PREFIX_URL = "http://localhost/authors";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @MockBean
    private AuthorMapper authorMapper;

    private final ObjectMapper objectMapper;

    public AuthorControllerTest(){

        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @Test
    void shouldGetPage() {

        //given

        //when

        //then
    }

    @Test
    void shouldGetAuthorBooksPage() {


    }

    @Test
    void shouldCreate() throws Exception {

        //given
        UUID publisherId = UUID.randomUUID();

        CreateAuthorRequest request = new CreateAuthorRequest(
            "Kamil",
            "Nowak",
            publisherId.toString()
        );

        PublisherEntity publisher = PublisherEntity.builder()
            .id(publisherId)
            .build();

        CreateAuthor createAuthor = new CreateAuthor(
            "Kamil",
            "Nowak",
            publisherId
        );

        AuthorEntity createdAuthor = AuthorEntity.builder()
            .firstname(request.firstname())
            .surname(request.surname())
            .mainPublisher(publisher)
            .build();

        String encodedRequest = objectMapper.writeValueAsString(request);

        //when
        Mockito.when(authorMapper.createAuthorRequestToCreateAuthor(any())).thenReturn(createAuthor);
        Mockito.when(authorService.create(any())).thenReturn(createdAuthor);

        MvcResult mvcResult = mockMvc
            .perform(
                post(API_PREFIX_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(encodedRequest)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        String gotEncodedResponse = mvcResult.getResponse().getContentAsString();
        AuthorHeader gotCreatedAuthorHeader = objectMapper.readValue(gotEncodedResponse, AuthorHeader.class);

        //then
        Mockito.verify(authorMapper).createAuthorRequestToCreateAuthor(request);
        Mockito.verify(authorService).create(createAuthor);
        Mockito.verify(authorMapper).authorEntityToAuthorHeader(createdAuthor);

        assertEquals(request.firstname(), createAuthor.firstname());
        assertEquals(request.surname(), createAuthor.surname());
        assertEquals(request.mainPublisherId(), createAuthor.mainPublisherId().toString());

        assertEquals(request.firstname(), gotCreatedAuthorHeader.firstname());
        assertEquals(request.surname(), gotCreatedAuthorHeader.surname());
        assertEquals(request.mainPublisherId(), gotCreatedAuthorHeader.mainPublisher().id());
    }

    @Test
    void shouldDeleteById() {


    }
}