package pl.books.magagement.model.api.request;

import jakarta.validation.constraints.NotBlank;

public record CreatePublisherRequest(

    @NotBlank(message = "Name is required")
    String name
){}
