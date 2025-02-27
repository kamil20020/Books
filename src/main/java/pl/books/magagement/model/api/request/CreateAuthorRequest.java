package pl.books.magagement.model.api.request;

import jakarta.validation.constraints.NotBlank;

public record CreateAuthorRequest(

    @NotBlank(message = "Firstname is required")
    String firstname,

    @NotBlank(message = "Surname is required")
    String surname,

    String mainPublisherId
){}
