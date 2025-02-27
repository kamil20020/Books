package pl.books.magagement.model.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.books.magagement.model.api.request.CreateBookRequest;
import pl.books.magagement.model.internal.CreateBook;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {Base64Mapper.class}
)
public interface BookMapper {

    @Mapping(source = "picture", target = "picture", qualifiedByName = "base64ToByteArray")
    CreateBook createBookRequestToCreateBook(CreateBookRequest createBookRequest);
}
