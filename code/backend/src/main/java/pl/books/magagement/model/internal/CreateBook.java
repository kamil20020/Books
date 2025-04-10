package pl.books.magagement.model.internal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateBook(
    String title,
    BigDecimal price,
    LocalDate publicationDate,
    byte[] picture,
    UUID publisherId,
    List<UUID> authorsIds
){}
