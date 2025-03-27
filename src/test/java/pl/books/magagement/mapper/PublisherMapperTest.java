package pl.books.magagement.mapper;

import org.junit.jupiter.api.Test;
import pl.books.magagement.model.api.response.PublisherHeader;
import pl.books.magagement.model.entity.PublisherEntity;
import pl.books.magagement.model.mappers.PublisherMapper;
import pl.books.magagement.model.mappers.PublisherMapperImpl;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PublisherMapperTest {

    private PublisherMapper publisherMapper = new PublisherMapperImpl();

    @Test
    void shouldConvertPublisherEntityToPublisherHeader() {

        //given
        PublisherEntity publisher = new PublisherEntity(UUID.randomUUID(), "Publisher 123");

        //when
        PublisherHeader gotPublisherHeader = publisherMapper.publisherEntityToPublisherHeader(publisher);

        //then
        assertEquals(publisher.getId().toString(), gotPublisherHeader.id());
        assertEquals(publisher.getName(), gotPublisherHeader.name());
    }

    @Test
    void shouldConvertPublisherEntityToPublisherHeaderWhenNull(){

        //given

        //when
        PublisherHeader gotPublisherHeader = publisherMapper.publisherEntityToPublisherHeader(null);

        //then
        assertNull(gotPublisherHeader);
    }
}