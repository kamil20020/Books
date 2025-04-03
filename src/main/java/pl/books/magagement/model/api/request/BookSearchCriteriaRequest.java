package pl.books.magagement.model.api.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookSearchCriteriaRequest(
    String title,
    String authorName,
    String publisherName,
    LocalDate publicationDate,
    BigDecimal price
){}
