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
import org.springframework.beans.factory.annotation.Autowired;
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

import java.awt.print.Book;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private PublisherService publisherService;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldGetBooksPage() {

        //given
        BookEntity book = BookEntity.builder()
            .id(UUID.randomUUID())
            .build();

        BookEntity book1 = BookEntity.builder()
            .id(UUID.randomUUID())
            .build();

        Pageable pageable = PageRequest.of(0, 5);

        Page<BookEntity> expectedBooksPage = new PageImpl<>(List.of(book, book1));

        //when
        Mockito.when(bookRepository.findAll(any(Pageable.class))).thenReturn(expectedBooksPage);

        Page<BookEntity> gotBooksPage = bookService.getBooksPage(pageable);

        //then
        assertEquals(2, gotBooksPage.getTotalElements());

        List<BookEntity> gotBooks = gotBooksPage.getContent();

        assertTrue(gotBooks.contains(book));
        assertTrue(gotBooks.contains(book1));

        Mockito.verify(bookRepository).findAll(pageable);
    }

    @Test
    void shouldCreate() {

        //given
        PublisherEntity publisher = new PublisherEntity(UUID.randomUUID(), "Publisher 123");

        AuthorEntity author = AuthorEntity.builder()
            .id(UUID.randomUUID())
            .publishedBooksCount((short) 0)
            .books(new HashSet<>())
            .build();

        AuthorEntity author1 = AuthorEntity.builder()
            .id(UUID.randomUUID())
            .publishedBooksCount((short) 0)
            .books(new HashSet<>())
            .build();

        List<UUID> authorsIds = List.of(author.getId(), author1.getId());

        String bookTitle = "Book 123";

        CreateBook createBook = new CreateBook(
            bookTitle,
            BigDecimal.valueOf(2.35),
            LocalDate.now(),
            null,
            publisher.getId(),
            authorsIds
        );

        BookEntity book = BookEntity.builder()
            .title(bookTitle)
            .price(createBook.price())
            .publicationDate(createBook.publicationDate())
            .picture(createBook.picture())
            .publisher(publisher)
            .authors(List.of(author, author1))
            .build();

        //when
        Mockito.when(publisherService.getById(any())).thenReturn(publisher);
        Mockito.when(authorRepository.findAllById(any())).thenReturn(List.of(author, author1));
        Mockito.when(bookRepository.existsByTitleIgnoreCase(anyString())).thenReturn(false);
        Mockito.when(bookRepository.save(any())).thenReturn(book);

        BookEntity createdBook = bookService.create(createBook);

        //then
        Mockito.verify(publisherService).getById(publisher.getId());
        Mockito.verify(authorRepository).findAllById(authorsIds);
        Mockito.verify(bookRepository).existsByTitleIgnoreCase(bookTitle);

        ArgumentCaptor<BookEntity> toCreateBookArgumentCaptor = ArgumentCaptor.forClass(BookEntity.class);

        Mockito.verify(bookRepository).save(toCreateBookArgumentCaptor.capture());

        BookEntity toCreateBook = toCreateBookArgumentCaptor.getValue();

        assertEquals(createBook.title(), toCreateBook.getTitle());
        assertEquals(createBook.price(), toCreateBook.getPrice());
        assertEquals(createBook.publicationDate(), toCreateBook.getPublicationDate());
        assertEquals(createBook.picture(), toCreateBook.getPicture());
        assertEquals(createBook.authorsIds(), toCreateBook.getAuthors().stream().map(a -> a.getId()).toList());
        assertEquals(createBook.publisherId(), toCreateBook.getPublisher().getId());

        assertEquals(createBook.title(), createdBook.getTitle());
        assertEquals(createBook.price(), createdBook.getPrice());
        assertEquals(createBook.publicationDate(), createdBook.getPublicationDate());
        assertEquals(createBook.picture(), createdBook.getPicture());
        assertEquals(createBook.authorsIds(), createdBook.getAuthors().stream().map(a -> a.getId()).toList());
        assertEquals(createBook.publisherId(), createdBook.getPublisher().getId());

        assertEquals(1, (int) author.getPublishedBooksCount());
        assertTrue(author.getBooks().contains(createdBook));

        assertEquals(1, (int) author1.getPublishedBooksCount());
        assertTrue(author1.getBooks().contains(createdBook));
    }

    @Test
    public void shouldNotCreateWhenPublisherDoesNotExist(){

        //given
        PublisherEntity publisher = new PublisherEntity(UUID.randomUUID(), "Publisher 123");

        CreateBook createBook = new CreateBook(
            "Book 123",
            BigDecimal.valueOf(2.35),
            LocalDate.now(),
            null,
            publisher.getId(),
            List.of()
        );

        //when
        Mockito.when(publisherService.getById(any())).thenThrow(EntityNotFoundException.class);

        //then
        assertThrows(
            EntityNotFoundException.class,
            () -> bookService.create(createBook)
        );

        Mockito.verify(publisherService).getById(publisher.getId());
    }

    @Test
    void shouldNotCreateBookWithDuplicateTitle(){

        //given
        PublisherEntity publisher = new PublisherEntity(UUID.randomUUID(), "Publisher 123");

        AuthorEntity author = AuthorEntity.builder()
            .id(UUID.randomUUID())
            .build();

        List<UUID> authorsIds = List.of(author.getId());

        CreateBook createBook = new CreateBook(
            "Book 123",
            BigDecimal.valueOf(2.35),
            LocalDate.now(),
            null,
            publisher.getId(),
            authorsIds
        );

        //when
        Mockito.when(publisherService.getById(any())).thenReturn(publisher);
        Mockito.when(bookRepository.existsByTitleIgnoreCase(anyString())).thenReturn(true);

        //then
        assertThrows(
            EntityExistsException.class,
            () -> bookService.create(createBook)
        );

        Mockito.verify(publisherService).getById(publisher.getId());
        Mockito.verify(authorRepository).findAllById(authorsIds);
        Mockito.verify(bookRepository).existsByTitleIgnoreCase(createBook.title());
    }

    @Test
    void shouldDeleteById() {

        //given
        AuthorEntity author = AuthorEntity.builder()
            .id(UUID.randomUUID())
            .publishedBooksCount((short) 1)
            .books(new HashSet<>())
            .build();

        AuthorEntity author1 = AuthorEntity.builder()
            .id(UUID.randomUUID())
            .publishedBooksCount((short) 1)
            .books(new HashSet<>())
            .build();

        UUID bookId = UUID.randomUUID();

        BookEntity book = BookEntity.builder()
            .id(bookId)
            .authors(List.of(author, author1))
            .build();

        author.getBooks().add(book);
        author1.getBooks().add(book);

        //when
        Mockito.when(bookRepository.findById(any())).thenReturn(Optional.of(book));

        bookService.deleteById(bookId);

        //then
        Mockito.verify(bookRepository).findById(bookId);

        assertEquals(0, (int) author.getPublishedBooksCount());
        assertTrue(author.getBooks().isEmpty());

        assertEquals(0, (int) author1.getPublishedBooksCount());
        assertTrue(author1.getBooks().isEmpty());
    }

    @Test
    public void shouldNotDeleteBookWhenBookIdWasNotFound(){

        //given
        UUID bookId = UUID.randomUUID();

        //when
        Mockito.when(bookRepository.findById(any())).thenReturn(Optional.empty());

        //then
        assertThrows(
            EntityNotFoundException.class,
            () -> bookService.deleteById(bookId)
        );

        Mockito.verify(bookRepository).findById(bookId);
    }
}