package pl.books.magagement.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.books.magagement.model.entity.PublisherEntity;

import java.util.UUID;

@Repository
public interface PublisherRepository extends ListCrudRepository<PublisherEntity, UUID>, ListPagingAndSortingRepository<PublisherEntity, UUID> {

    boolean existsByNameIgnoreCase(String name);
}
