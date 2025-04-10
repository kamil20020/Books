package pl.books.magagement.model.api.request;

import jakarta.validation.constraints.NotBlank;

public record PatchUserRequest(
    String username
){}
