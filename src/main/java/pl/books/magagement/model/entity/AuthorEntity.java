package pl.books.magagement.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "AUTHORS")
@NoArgsConstructor
@AllArgsConstructor
public class AuthorEntity {

    @Id
    @Column(name = "author_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private Short publishedBooksCount;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "main_publisher_id")
    private PublisherEntity mainPublisher;

    @JsonIgnore
    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    private Set<BookEntity> books;

    @Override
    public boolean equals(Object o) {

        if (this == o){
            return true;
        }

        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)){
            return false;
        }

        AuthorEntity convertedO = (AuthorEntity) o;

        return Objects.equals(id, convertedO.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
};
