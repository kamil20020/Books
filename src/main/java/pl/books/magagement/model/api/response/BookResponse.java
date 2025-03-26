package pl.books.magagement.model.api.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record BookResponse (
    String id,
    String title,
    BigDecimal price,
    String picture,
    LocalDate publicationDate,
    PublisherHeader publisher,
    List<AuthorHeader> authors
){}
