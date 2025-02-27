package pl.books.magagement.model.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import pl.books.magagement.model.api.request.CreateAuthorRequest;
import pl.books.magagement.model.api.response.AuthorHeader;
import pl.books.magagement.model.api.response.PublisherHeader;
import pl.books.magagement.model.entity.AuthorEntity;
import pl.books.magagement.model.entity.PublisherEntity;
import pl.books.magagement.model.internal.CreateAuthor;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {PublisherMapper.class}
)
public interface AuthorMapper {

    CreateAuthor createAuthorRequestToCreateAuthor(CreateAuthorRequest request);

    AuthorHeader authorEntityToAuthorHeader(AuthorEntity authorEntity);
}
