package pl.books.magagement.model.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.books.magagement.model.api.request.BookSearchCriteriaRequest;
import pl.books.magagement.model.api.request.CreateBookRequest;
import pl.books.magagement.model.api.response.BookResponse;
import pl.books.magagement.model.entity.BookEntity;
import pl.books.magagement.model.internal.BookSearchCriteria;
import pl.books.magagement.model.internal.CreateBook;

import java.util.List;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {Base64Mapper.class, AuthorMapper.class, PublisherMapper.class}
)
public interface BookMapper {

    @Mapping(source = "picture", target = "picture", qualifiedByName = "byteArrayToBase64")
    BookResponse bookEntityToBookResponse(BookEntity book);

    BookSearchCriteria bookSearchCriteriaRequestToBookSearchCriteria(BookSearchCriteriaRequest request);

    @Mapping(source = "picture", target = "picture", qualifiedByName = "base64ToByteArray")
    CreateBook createBookRequestToCreateBook(CreateBookRequest createBookRequest);

    List<BookResponse> bookEntityListToBookResponseList(List<BookEntity> bookEntities);
}
