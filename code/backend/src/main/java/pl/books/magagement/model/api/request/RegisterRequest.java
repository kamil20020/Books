package pl.books.magagement.model.api.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(

    @NotBlank(message = "Username is required")
    String username,

    @NotBlank(message = "Password is required")
    String password
){}
