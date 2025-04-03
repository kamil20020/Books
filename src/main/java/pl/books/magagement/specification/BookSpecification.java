package pl.books.magagement.specification;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import pl.books.magagement.model.entity.AuthorEntity;
import pl.books.magagement.model.entity.AuthorEntity_;
import pl.books.magagement.model.entity.BookEntity;
import pl.books.magagement.model.entity.BookEntity_;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

public class BookSpecification {

    public static Specification<BookEntity> matchTitle(String title){

        return (root, query, builder) -> {

            if(title == null){
                return builder.and();
            }

            String convertedTitle = title.toLowerCase();

            Path<String> valuePath = root.get(BookEntity_.TITLE);
            Expression<String> titleExpression = builder.lower(valuePath);

            return builder.like(titleExpression, "%" + convertedTitle + "%");
        };
    }

    public static Specification<BookEntity> matchAuthorField(String value, String fieldName){

        return (root, query, builder) -> {

            if(value == null){
                return builder.and();
            }

            String convertedValue = value.toLowerCase();

            Join<BookEntity, AuthorEntity> authorsJoined = root.join(BookEntity_.AUTHORS);

            Path<String> authorsValuePath = authorsJoined.get(fieldName);

            Expression<String> authorsValueExpression = builder.lower(authorsValuePath);

            return builder.like(authorsValueExpression, "%" + convertedValue + "%");
        };
    }

    public static Specification<BookEntity> matchAuthorFirstname(String firstname){

        return matchAuthorField(firstname, AuthorEntity_.FIRSTNAME);
    }

    public static Specification<BookEntity> matchAuthorSurname(String surname){

        return matchAuthorField(surname, AuthorEntity_.SURNAME);
    }

    public static Specification<BookEntity> matchAuthorName(String name){

        Specification<BookEntity> emptySpecification = Specification.anyOf();

        if(name == null){
            return emptySpecification;
        }

        String[] splitedName = name.split("\\s");

        if(splitedName.length == 0){
            return emptySpecification;
        }

        if(splitedName.length == 1){

            return Specification.anyOf(
                matchAuthorFirstname(splitedName[0]),
                matchAuthorSurname(splitedName[0])
            );
        }

        return Specification.anyOf(
            Specification.allOf(
                matchAuthorFirstname(splitedName[0]),
                matchAuthorSurname(splitedName[1])
            ),
            Specification.allOf(
                matchAuthorFirstname(splitedName[1]),
                matchAuthorSurname(splitedName[0])
            )
        );
    }

    public static Specification<BookEntity> matchLessPublicationDate(LocalDate publicationDate){

        if(publicationDate == null){
            return Specification.anyOf();
        }

        int year = publicationDate.getYear();

        LocalDate lastDayOfYear = LocalDate.of(year, Month.DECEMBER, 31);

        return (root, query, builder) -> {

            Path<LocalDate> publicationDatePath = root.get(BookEntity_.PUBLICATION_DATE);

            return builder.lessThanOrEqualTo(publicationDatePath, lastDayOfYear);
        };
    }

    public static Specification<BookEntity> matchLessPrice(BigDecimal price){

        if(price == null){
            return Specification.anyOf();
        }

        return (root, query, builder) -> {

            Path<BigDecimal> pricePath = root.get(BookEntity_.PRICE);

            return builder.lessThanOrEqualTo(pricePath, price);
        };
    }
}
