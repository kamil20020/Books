package pl.books.magagement.specification;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import pl.books.magagement.model.entity.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

public class BookSpecification {

    public static Specification<BookEntity> matchTitle(String title){

        if(title == null){
            return Specification.anyOf();
        }

        return (root, query, builder) -> {

            String convertedTitle = title.trim().toLowerCase();

            Path<String> valuePath = root.get(BookEntity_.TITLE);
            Expression<String> titleExpression = builder.lower(valuePath);

            return builder.like(titleExpression, "%" + convertedTitle + "%");
        };
    }

    public static Specification<BookEntity> matchAuthorField(String value, String fieldName){

        if(value == null){
            return Specification.anyOf();
        }

        return (root, query, builder) -> {

            query.distinct(true);

            String convertedValue = value.trim().toLowerCase();

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

        String[] splitedName = name.trim().split("\\s");

        if(splitedName.length == 0 || splitedName.length > 2){
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

    public static Specification<BookEntity> matchPublisherName(String publisherName){

        if(publisherName == null){
            return Specification.anyOf();
        }

        return (root, query, builder) -> {

            String lowerSearchedPublisherName = publisherName.trim().toLowerCase();

            Join<BookEntity, PublisherEntity> joinedPublisher = root.join(BookEntity_.PUBLISHER);
            Path<String> publisherNamePath = joinedPublisher.get(PublisherEntity_.NAME);

            Expression<String> lowerPublisherName = builder.lower(publisherNamePath);

            return builder.like(lowerPublisherName, "%" + lowerSearchedPublisherName + "%");
        };
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
