package pl.books.magagement.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.books.magagement.model.entity.AuthorEntity;
import pl.books.magagement.model.entity.BookEntity;
import pl.books.magagement.model.entity.PublisherEntity;
import pl.books.magagement.model.internal.CreateAuthor;
import pl.books.magagement.repository.AuthorRepository;
import pl.books.magagement.repository.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PublisherService publisherService;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void shouldGetById() {

        //given
        UUID authorId = UUID.randomUUID();

        AuthorEntity author = AuthorEntity.builder()
            .id(authorId)
            .build();

        //when
        Mockito.when(authorRepository.findById(any())).thenReturn(Optional.of(author));

        AuthorEntity gotAuthor = authorService.getById(authorId);

        //then
        assertEquals(author, gotAuthor);

        Mockito.verify(authorRepository).findById(authorId);
    }

    @Test
    public void shouldNotGetByNotFoundId(){

        //given
        UUID authorId = UUID.randomUUID();

        //when
        Mockito.when(authorRepository.findById(any())).thenReturn(Optional.empty());

        //then
        assertThrows(
            EntityNotFoundException.class,
            () -> authorService.getById(authorId)
        );

        Mockito.verify(authorRepository).findById(authorId);
    }

    @Test
    void shouldGetPage() {

        //given
        Pageable pageable = PageRequest.of(0, 5);

        AuthorEntity a1 = AuthorEntity.builder()
            .id(UUID.randomUUID())
            .firstname("Kamil")
            .surname("Nowak")
            .build();

        AuthorEntity a2 = AuthorEntity.builder()
            .id(UUID.randomUUID())
            .firstname("Adam")
            .surname("Kowalski")
            .build();

        Page<AuthorEntity> authorsPage = new PageImpl<>(
            List.of(
                a1, a2
            )
        );

        //when
        Mockito.when(authorRepository.findAll(any(Pageable.class))).thenReturn(authorsPage);

        Page<AuthorEntity> gotAuthorsPage = authorService.getPage(pageable);

        //then
        Mockito.verify(authorRepository).findAll(pageable);

        assertEquals(authorsPage.getTotalElements(), gotAuthorsPage.getTotalElements());
        assertTrue(
            gotAuthorsPage.getContent().containsAll(
                authorsPage.getContent()
            )
        );
    }

    @Test
    void getAuthorBooksPage() {

        //given
        UUID authorId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 5);

        BookEntity b1 = BookEntity.builder()
            .id(UUID.randomUUID())
            .build();

        BookEntity b2 = BookEntity.builder()
            .id(UUID.randomUUID())
            .build();

        Page<BookEntity> authorsBooksPage = new PageImpl<>(
            List.of(b1, b2)
        );

        //when
        Mockito.when(authorRepository.existsById(any())).thenReturn(true);
        Mockito.when(bookRepository.findAllByAuthorsIdIn(any(), any())).thenReturn(authorsBooksPage);

        Page<BookEntity> gotAuthorsBooksPage = authorService.getAuthorBooksPage(authorId, pageable);

        //then
        Mockito.verify(authorRepository).existsById(authorId);
        Mockito.verify(bookRepository).findAllByAuthorsIdIn(List.of(authorId), pageable);

        assertEquals(authorsBooksPage.getTotalElements(), gotAuthorsBooksPage.getTotalElements());
        assertTrue(
            gotAuthorsBooksPage.getContent().containsAll(
                authorsBooksPage.getContent()
            )
        );
    }

    @Test
    void shouldCreate() {

        //given
        UUID authorId = UUID.randomUUID();
        UUID publisherId = UUID.randomUUID();

        CreateAuthor createAuthor = new CreateAuthor(
            "Kamil",
            "Nowak",
            publisherId
        );

        PublisherEntity publisher = PublisherEntity.builder()
            .id(publisherId)
            .build();

        AuthorEntity author = AuthorEntity.builder()
            .id(authorId)
            .firstname(createAuthor.firstname())
            .surname(createAuthor.surname())
            .mainPublisher(publisher)
            .build();

        //when
        Mockito.when(authorRepository.save(any())).thenReturn(author);
        Mockito.when(publisherService.getById(any())).thenReturn(publisher);

        AuthorEntity createdAuthor = authorService.create(createAuthor);

        //then
        Mockito.verify(publisherService).getById(publisherId);

        ArgumentCaptor<AuthorEntity> authorToSaveCaptor = ArgumentCaptor.forClass(AuthorEntity.class);

        Mockito.verify(authorRepository).save(authorToSaveCaptor.capture());

        AuthorEntity givenToSaveAuthor = authorToSaveCaptor.getValue();

        assertEquals(authorId, createdAuthor.getId());
        assertEquals(createAuthor.firstname(), createdAuthor.getFirstname());
        assertEquals(createAuthor.surname(), createdAuthor.getSurname());
        assertEquals(createAuthor.mainPublisherId(), createdAuthor.getMainPublisher().getId());

        assertEquals(createAuthor.firstname(), givenToSaveAuthor.getFirstname());
        assertEquals(createAuthor.surname(), givenToSaveAuthor.getSurname());
        assertEquals(createAuthor.mainPublisherId(), givenToSaveAuthor.getMainPublisher().getId());
        assertEquals((short) 0, givenToSaveAuthor.getPublishedBooksCount());
    }

    @Test
    void shouldDeleteById() {

        //given
        UUID authorId = UUID.randomUUID();

        AuthorEntity author = AuthorEntity.builder()
            .id(authorId)
            .build();

        //when
        Mockito.when(authorRepository.findById(any())).thenReturn(Optional.of(author));

        authorService.deleteById(authorId);

        //then
        Mockito.verify(authorRepository).findById(authorId);
        Mockito.verify(authorRepository).deleteById(authorId);
    }

    @Test
    void shouldNotDeleteByIdWhenAuthorWasNotFound(){

        //given
        UUID authorId = UUID.randomUUID();

        //when
        Mockito.when(authorRepository.findById(any())).thenReturn(Optional.empty());

        //then
        assertThrows(
            EntityNotFoundException.class,
            () -> authorService.deleteById(authorId)
        );

        Mockito.verify(authorRepository).findById(authorId);
    }
}