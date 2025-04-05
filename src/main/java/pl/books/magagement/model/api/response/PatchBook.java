package pl.books.magagement.model.api.response;

import java.math.BigDecimal;

public record PatchBook(
    String title,
    BigDecimal price,
    byte[] picture
){}
