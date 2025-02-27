package pl.books.magagement.model.internal;

import java.util.UUID;

public record CreateAuthor(
    String firstname,
    String surname,
    UUID mainPublisherId
){}
