package pl.books.magagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.books.magagement.model.entity.AuthorEntity;
import pl.books.magagement.model.entity.BookEntity;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface BookRepository extends CrudRepository<BookEntity, UUID>, PagingAndSortingRepository<BookEntity, UUID>, JpaSpecificationExecutor<BookEntity> {

    boolean existsByTitleIgnoreCase(String title);
    Page<BookEntity> findAllByPublisherId(UUID publisherId, Pageable pageable);
    Page<BookEntity> findAllByAuthorsIdIn(List<UUID> authors, Pageable pageable);
}
