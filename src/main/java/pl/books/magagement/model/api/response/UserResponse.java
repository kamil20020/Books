package pl.books.magagement.model.api.response;

import pl.books.magagement.model.entity.RoleEntity;

import java.util.List;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String username,
    List<RoleEntity> roles
){}
