package pl.books.magagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.books.magagement.model.entity.RevokedRefreshTokenEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface RevokedRefreshTokenRepository extends JpaRepository<RevokedRefreshTokenEntity, UUID> {

    void deleteAllByUserId(UUID userId);
    void deleteAllByExpirationDateBefore(LocalDateTime actualDate);
}
