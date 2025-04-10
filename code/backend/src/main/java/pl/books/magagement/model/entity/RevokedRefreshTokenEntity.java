package pl.books.magagement.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "REVOKED_REFRESH_TOKENS")
public class RevokedRefreshTokenEntity {

    @Id
    @Column(name = "token_id")
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RevokedRefreshTokenEntity that = (RevokedRefreshTokenEntity) o;
        return id.equals(that.id) && userId.equals(that.userId) && expirationDate.equals(that.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, expirationDate);
    }
}
