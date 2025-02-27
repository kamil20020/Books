package pl.books.magagement.model.api.response;

public record AuthorHeader(
    String id,
    String firstname,
    String surname,
    Short publishedBooksCount,
    PublisherHeader mainPublisher
){}
