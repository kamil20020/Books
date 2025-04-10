package pl.books.magagement.model.api.request;

import java.math.BigDecimal;

public record PatchBookRequest(
    String title,
    BigDecimal price,
    String picture
){}
