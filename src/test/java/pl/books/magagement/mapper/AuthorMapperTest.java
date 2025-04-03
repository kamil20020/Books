package pl.books.magagement.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.books.magagement.model.api.response.AuthorHeader;
import pl.books.magagement.model.api.response.PublisherHeader;
import pl.books.magagement.model.entity.AuthorEntity;
import pl.books.magagement.model.entity.PublisherEntity;
import pl.books.magagement.model.mappers.AuthorMapper;
import pl.books.magagement.model.mappers.AuthorMapperImpl;
import pl.books.magagement.model.mappers.PublisherMapperImpl;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AuthorMapperTest {

    @Mock
    private PublisherMapperImpl publisherMapper;

    @InjectMocks
    private AuthorMapperImpl authorMapper;

    @Test
    public void shouldConvertAuthorEntityToAuthorHeaderWithMainPublisher(){

        //given
        PublisherEntity publisher = new PublisherEntity(UUID.randomUUID(), "Publisher 123");

        AuthorEntity author = AuthorEntity.builder()
            .id(UUID.randomUUID())
            .firstname("kamil")
            .surname("nowak")
            .publishedBooksCount((short) 2)
            .mainPublisher(publisher)
            .build();

        PublisherHeader publisherHeader = new PublisherHeader(publisher.getId().toString(), publisher.getName());

        //when
        Mockito.when(publisherMapper.publisherEntityToPublisherHeader(any())).thenReturn(publisherHeader);

        AuthorHeader gotAuthorHeader = authorMapper.authorEntityToAuthorHeader(author);

        //then
        assertNotNull(gotAuthorHeader.mainPublisher());
        assertEquals(publisher.getId().toString(), gotAuthorHeader.mainPublisher().id());
        assertEquals(publisher.getName(), gotAuthorHeader.mainPublisher().name());

        Mockito.verify(publisherMapper).publisherEntityToPublisherHeader(publisher);
    }

    @Test
    public void shouldConvertAuthorEntityToAuthorHeaderWithNoMainPublisher(){

        //given
        AuthorEntity author = AuthorEntity.builder()
            .id(UUID.randomUUID())
            .firstname("kamil")
            .surname("nowak")
            .publishedBooksCount((short) 2)
            .mainPublisher(null)
            .build();

        //when
        AuthorHeader gotAuthorHeader = authorMapper.authorEntityToAuthorHeader(author);

        //then
        assertEquals(author.getId().toString(), gotAuthorHeader.id());
        assertEquals(author.getFirstname(), gotAuthorHeader.firstname());
        assertEquals(author.getSurname(), gotAuthorHeader.surname());
        assertEquals(author.getPublishedBooksCount(), gotAuthorHeader.publishedBooksCount());
        assertNull(gotAuthorHeader.mainPublisher());

        Mockito.verify(publisherMapper).publisherEntityToPublisherHeader(null);
    }
}
