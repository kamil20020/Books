package pl.books.magagement.model.api.request;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreateBookRequest(

    @NotBlank(message = "Title is required")
    String title,

    @DecimalMin(value = "0.0", inclusive = false, message = "Too small price")
    @Digits(integer = 6, fraction = 2, message = "Invalid number of digits in price")
    @NotNull(message = "Price is required")
    BigDecimal price,

    @NotNull(message = "Publication date is required")
    LocalDate publicationDate,

    String picture,

    @UUID(message = "Invalid publisher id")
    @NotEmpty(message = "Publisher id is required")
    String publisherId,

    @NotEmpty(message = "Authors ids are required")
    List<String> authorsIds
){}
