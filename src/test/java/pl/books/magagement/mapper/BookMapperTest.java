package pl.books.magagement.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.books.magagement.model.api.request.BookSearchCriteriaRequest;
import pl.books.magagement.model.api.request.CreateBookRequest;
import pl.books.magagement.model.api.response.AuthorHeader;
import pl.books.magagement.model.api.response.BookResponse;
import pl.books.magagement.model.api.response.PublisherHeader;
import pl.books.magagement.model.entity.AuthorEntity;
import pl.books.magagement.model.entity.BookEntity;
import pl.books.magagement.model.entity.PublisherEntity;
import pl.books.magagement.model.internal.BookSearchCriteria;
import pl.books.magagement.model.internal.CreateBook;
import pl.books.magagement.model.mappers.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class BookMapperTest {

    @InjectMocks
    private BookMapperImpl bookMapper;

    @Mock
    private Base64MapperImpl base64Mapper;

    @Mock
    private AuthorMapperImpl authorMapper;

    @Mock
    private PublisherMapperImpl publisherMapper;

    @Test
    public void shouldConvertBookEntityToResponse(){

        //given
        PublisherEntity publisher = new PublisherEntity(UUID.randomUUID(), "Publisher 123");

        AuthorEntity author = AuthorEntity.builder()
            .id(UUID.randomUUID())
            .firstname("Kamil")
            .surname("Nowak")
            .publishedBooksCount((short) 1)
            .mainPublisher(publisher)
            .build();

        List<AuthorEntity> authors = List.of(author);

        byte[] rawPicture = "raw picture".getBytes(StandardCharsets.UTF_8);

        PublisherHeader publisherHeader = new PublisherHeader(publisher.getId().toString(), publisher.getName());

        AuthorHeader authorHeader = new AuthorHeader(
            author.getId().toString(),
            author.getFirstname(),
            author.getSurname(),
            author.getPublishedBooksCount(),
            publisherHeader
        );

        List<AuthorHeader> authorsHeaders = List.of(authorHeader);

        String encodedPicture = "encoded picture";

        BookEntity book = BookEntity.builder()
            .id(UUID.randomUUID())
            .title("Title 123")
            .publicationDate(LocalDate.now())
            .price(BigDecimal.valueOf(22.48))
            .picture(rawPicture)
            .authors(authors)
            .publisher(publisher)
            .build();

        //when
        Mockito.when(base64Mapper.byteArrayToBase64(any())).thenReturn(encodedPicture);
        Mockito.when(authorMapper.authorsEntitiesToAuthorsHeaders(any())).thenReturn(authorsHeaders);
        Mockito.when(publisherMapper.publisherEntityToPublisherHeader(any())).thenReturn(publisherHeader);

        BookResponse gotBookResponse = bookMapper.bookEntityToBookResponse(book);

        //then
        Mockito.verify(base64Mapper).byteArrayToBase64(rawPicture);
        Mockito.verify(authorMapper).authorsEntitiesToAuthorsHeaders(authors);
        Mockito.verify(publisherMapper).publisherEntityToPublisherHeader(publisher);

        assertEquals(book.getId().toString(), gotBookResponse.id());
        assertEquals(book.getTitle(), gotBookResponse.title());
        assertEquals(book.getPrice(), gotBookResponse.price());
        assertEquals(book.getPublicationDate(), gotBookResponse.publicationDate());
        assertEquals(encodedPicture, gotBookResponse.picture());
        assertTrue(gotBookResponse.authors().containsAll(authorsHeaders));
        assertEquals(publisherHeader, gotBookResponse.publisher());
    }

    @Test
    public void shouldConvertBookSearchCriteriaRequestToInternal(){

        //given
        BookSearchCriteriaRequest request = new BookSearchCriteriaRequest(
            "Title 123",
            "Kamil Nowak",
            "Publisher 123",
            LocalDate.now(),
            BigDecimal.valueOf(2248, 2)
        );

        //when
        BookSearchCriteria gotSearchCriteria = bookMapper.bookSearchCriteriaRequestToBookSearchCriteria(request);

        //then
        assertEquals(request.title(), gotSearchCriteria.title());
        assertEquals(request.authorName(), gotSearchCriteria.authorName());
        assertEquals(request.publisherName(), gotSearchCriteria.publisherName());
        assertEquals(request.publicationDate(), gotSearchCriteria.publicationDate());
        assertEquals(request.price(), gotSearchCriteria.price());
    }

    @Test
    public void shouldConvertCreateBookRequestToInternal(){

        //given
        String encodedPicture = "encoded picture";
        byte[] rawPicture = "raw picture".getBytes(StandardCharsets.UTF_8);

        CreateBookRequest request = new CreateBookRequest(
            "Book 123",
            BigDecimal.valueOf(2248, 2),
            LocalDate.now(),
            encodedPicture,
            UUID.randomUUID().toString(),
            List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString())
        );

        //when
        Mockito.when(base64Mapper.base64ToByteArray(any())).thenReturn(rawPicture);

        CreateBook gotCreateBook = bookMapper.createBookRequestToCreateBook(request);

        //then
        Mockito.verify(base64Mapper).base64ToByteArray(encodedPicture);

        assertEquals(request.title(), gotCreateBook.title());
        assertEquals(request.price(), gotCreateBook.price());
        assertEquals(request.publicationDate(), gotCreateBook.publicationDate());
        assertEquals(rawPicture, gotCreateBook.picture());
        assertEquals(request.publisherId(), gotCreateBook.publisherId().toString());
        assertTrue(gotCreateBook.authorsIds().stream()
            .map(authorId -> authorId.toString())
            .collect(Collectors.toList())
            .containsAll(request.authorsIds())
        );
    }

    @Test
    public void shouldConvertBookEntitesToResponses(){

        //given
        List<BookEntity> books = List.of(
            BookEntity.builder()
                .id(UUID.randomUUID())
                .build(),
            BookEntity.builder()
                .id(UUID.randomUUID())
                .build()
        );

        //when
        List<BookResponse> gotBooksResponses = bookMapper.bookEntityListToBookResponseList(books);

        //then
        assertEquals(2, gotBooksResponses.size());
        assertTrue(gotBooksResponses.stream()
                .map(BookResponse::id)
                .toList()
            .containsAll(books.stream()
                .map(book -> book.getId().toString())
                .toList()
            )
        );
    }
}
