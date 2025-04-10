package pl.books.magagement.model.api.request;

import java.util.List;

public record UserSearchCriteriaRequest(
    String username,
    String[] rolesIds
){}
