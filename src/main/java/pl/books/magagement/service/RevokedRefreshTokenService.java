package pl.books.magagement.service;

import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.books.magagement.config.JwtService;
import pl.books.magagement.model.entity.RevokedRefreshTokenEntity;
import pl.books.magagement.repository.RevokedRefreshTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RevokedRefreshTokenService {

    private final RevokedRefreshTokenRepository revokedRefreshTokenRepository;

    private final JwtService jwtService;

    public boolean doesTokenExist(String token){

        UUID tokenId = jwtService.getTokenId(token);

        return revokedRefreshTokenRepository.existsById(tokenId);
    }

    @Transactional
    public void addToken(String token){

        UUID tokenId = jwtService.getTokenId(token);
        UUID userId = jwtService.getUserId(token);
        LocalDateTime tokenExpirationTime = jwtService.getExpirationTime(token);

        RevokedRefreshTokenEntity revokedToken = RevokedRefreshTokenEntity.builder()
            .id(tokenId)
            .userId(userId)
            .expirationDate(tokenExpirationTime)
            .build();

        revokedRefreshTokenRepository.save(revokedToken);
    }

    public void removeTokensForUser(UUID userId){

        revokedRefreshTokenRepository.deleteAllByUserId(userId);
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    @Transactional
    public void removeExpiredTokens(){

        LocalDateTime actualDate = LocalDateTime.now();

        revokedRefreshTokenRepository.deleteAllByExpirationDateAfter(actualDate);
    }
}
