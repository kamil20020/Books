package pl.books.magagement.model.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import pl.books.magagement.model.api.response.PublisherHeader;
import pl.books.magagement.model.entity.PublisherEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PublisherMapper {

    PublisherHeader publisherEntityToPublisherHeader(PublisherEntity publisherEntity);
}
