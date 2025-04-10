package pl.books.magagement.model.internal;

import java.util.List;
import java.util.UUID;

public record UserSearchCriteria(
    String username,
    UUID[] rolesIds
){}
