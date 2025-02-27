package pl.books.magagement.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "PUBLISHERS")
@NoArgsConstructor
@AllArgsConstructor
public class PublisherEntity {

    @Id
    @Column(name = "publisher_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    public PublisherEntity(String name){
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PublisherEntity publisher = (PublisherEntity) o;
        return id != null && Objects.equals(id, publisher.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
