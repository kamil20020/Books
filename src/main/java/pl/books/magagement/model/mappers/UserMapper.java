package pl.books.magagement.model.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import pl.books.magagement.model.api.request.PatchUserRequest;
import pl.books.magagement.model.api.request.UserSearchCriteriaRequest;
import pl.books.magagement.model.api.response.UserResponse;
import pl.books.magagement.model.entity.UserEntity;
import pl.books.magagement.model.internal.PatchUser;
import pl.books.magagement.model.internal.UserSearchCriteria;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserSearchCriteria userSearchCriteriaRequestToUserSearchCriteria(UserSearchCriteriaRequest criteriaRequest);

    PatchUser patchUserRequestToPatchUser(PatchUserRequest request);

    UserResponse userEntityToUserResponse(UserEntity user);
}
