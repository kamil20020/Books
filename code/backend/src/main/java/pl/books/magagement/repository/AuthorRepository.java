package pl.books.magagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.books.magagement.model.entity.AuthorEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuthorRepository extends ListCrudRepository<AuthorEntity, UUID>, PagingAndSortingRepository<AuthorEntity, UUID> {

    Page<AuthorEntity> findAllByMainPublisherId(UUID mainPublisherId, Pageable pageable);
}
