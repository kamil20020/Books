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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.books.magagement.model.entity.AuthorEntity;
import pl.books.magagement.model.entity.BookEntity;
import pl.books.magagement.model.entity.PublisherEntity;
import pl.books.magagement.model.internal.CreateBook;
import pl.books.magagement.repository.AuthorRepository;
import pl.books.magagement.repository.BookRepository;
import pl.books.magagement.repository.PublisherRepository;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class PublisherServiceTest {

    @InjectMocks
    private PublisherService publisherService;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Test
    void shouldGetPage() {

        //given
        Pageable pageable = PageRequest.of(0, 5);

        Page<PublisherEntity> publishersPage = new PageImpl(
            List.of(
                new PublisherEntity(UUID.randomUUID(), "Publisher 123"),
                new PublisherEntity(UUID.randomUUID(), "Publisher 1234")
            ),
            pageable,
            2
        );

        //when
        Mockito.when(publisherRepository.findAll(any(Pageable.class))).thenReturn(publishersPage);

        Page<PublisherEntity> gotPublishersPage = publisherService.getPage(pageable);

        //then
        assertEquals(2, gotPublishersPage.getTotalElements());
        assertTrue(gotPublishersPage.getContent().containsAll(publishersPage.getContent()));

        Mockito.verify(publisherRepository).findAll(pageable);
    }

    @Test
    void shouldGetPublisherAuthors() {

        //given
        UUID publisherID = UUID.randomUUID();

        Pageable pageable = PageRequest.of(0, 5);

        Page<AuthorEntity> authorsPage = new PageImpl<>(
            List.of(
                AuthorEntity.builder()
                    .id(UUID.randomUUID())
                    .build(),
                AuthorEntity.builder()
                    .id(UUID.randomUUID())
                    .build()
            ),
            pageable,
            2
        );

        //when
        Mockito.when(publisherRepository.existsById(any())).thenReturn(true);
        Mockito.when(authorRepository.findAllByMainPublisherId(any(), any())).thenReturn(authorsPage);

        Page<AuthorEntity> gotAuthorsPage = publisherService.getPublisherAuthors(publisherID, pageable);

        //then
        Mockito.verify(publisherRepository).existsById(publisherID);
        Mockito.verify(authorRepository).findAllByMainPublisherId(publisherID, pageable);

        assertEquals(2, gotAuthorsPage.getTotalElements());
        assertTrue(gotAuthorsPage.getContent().containsAll(authorsPage.getContent()));
    }

    @Test
    void shouldGetById() {

        //given
        UUID publisherId = UUID.randomUUID();
        PublisherEntity publisher = new PublisherEntity(publisherId, "Publisher 123");
        Optional<PublisherEntity> publisherOpt = Optional.of(publisher);

        //when
        Mockito.when(publisherRepository.findById(any())).thenReturn(publisherOpt);

        PublisherEntity gotPublisher = publisherService.getById(publisherId);

        //then
        Mockito.verify(publisherRepository).findById(publisherId);

        assertEquals(publisher.getId(), gotPublisher.getId());
        assertEquals(publisher.getName(), gotPublisher.getName());
    }

    @Test
    public void shouldNotGetByIdWhenIdDoesNotExist(){

        //given
        UUID publisherId = UUID.randomUUID();

        //when
        Mockito.when(publisherRepository.findById(any())).thenReturn(Optional.empty());

        //then
        assertThrows(
            EntityNotFoundException.class,
            () -> publisherService.getById(publisherId)
        );

        Mockito.verify(publisherRepository).findById(publisherId);
    }

    @Test
    void shouldCreate() {

        //given
        String publisherName = "Publisher 123";

        PublisherEntity publisher = new PublisherEntity(UUID.randomUUID(), publisherName);

        //when
        Mockito.when(publisherRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);
        Mockito.when(publisherRepository.save(any())).thenReturn(publisher);

        PublisherEntity createdPublisher = publisherService.create(publisherName);

        //then
        Mockito.verify(publisherRepository).existsByNameIgnoreCase(publisherName);

        ArgumentCaptor<PublisherEntity> publisherCreateCaptor = ArgumentCaptor.forClass(PublisherEntity.class);
        Mockito.verify(publisherRepository).save(publisherCreateCaptor.capture());
        PublisherEntity toCreatePublisher = publisherCreateCaptor.getValue();

        assertEquals(publisher.getId(), createdPublisher.getId());
        assertEquals(publisher.getName(), createdPublisher.getName());

        assertEquals(publisher.getName(), toCreatePublisher.getName());
    }

    @Test
    public void shouldNotCreateDuplicatePublisher(){

        //given
        String publisherName = "Publisher 123";

        //when
        Mockito.when(publisherRepository.existsByNameIgnoreCase(anyString())).thenReturn(true);

        //then
        assertThrows(
            EntityExistsException.class,
            () -> publisherService.create(publisherName)
        );

        Mockito.verify(publisherRepository).existsByNameIgnoreCase(publisherName);
    }

    @Test
    void shouldDeleteById() {

        //given
        UUID publisherId = UUID.randomUUID();

        //when
        Mockito.when(publisherRepository.existsById(any())).thenReturn(true);

        publisherService.deleteById(publisherId);

        //then
        Mockito.verify(publisherRepository).existsById(publisherId);
        Mockito.verify(publisherRepository).deleteById(publisherId);
    }
}