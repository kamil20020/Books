package pl.books.magagement.model.internal;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookSearchCriteria(
    String title,
    String authorName,
    String publisherName,
    LocalDate publicationDate,
    BigDecimal price
){}
