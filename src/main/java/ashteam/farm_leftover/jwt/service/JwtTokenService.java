package ashteam.farm_leftover.jwt.service;

import ashteam.farm_leftover.jwt.dao.AccessTokenRepository;
import ashteam.farm_leftover.jwt.dao.RefreshTokenRepository;
import ashteam.farm_leftover.jwt.model.AccessToken;
import ashteam.farm_leftover.jwt.model.RefreshToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenService {

    private static final Logger LOGGER = LogManager.getLogger(JwtTokenService.class);
    private static final String secret = "25bf957ee14c22b0b22711ff49b34a705227433040c82c4112f6e6dbdc443746";
    private static final int accessTokenExpirationMinutes = 15;
    private static final int refreshTokenExpirationDays = 7;

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public String generateAccessToken(String login) {
        return buildToken(login, accessTokenExpirationMinutes, ChronoUnit.MINUTES);
    }

    public String generateRefreshToken(String login) {
        return buildToken(login, refreshTokenExpirationDays, ChronoUnit.DAYS);
    }

    private String buildToken(String login, int expiration, ChronoUnit unit) {
        Instant now = Instant.now();
        Instant expiry = now.plus(expiration, unit);

        return Jwts.builder()
                .subject(login)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(getSignKey())
                .compact();
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            if (isTokenExpired(accessToken)) {
                revokeAccessToken(accessToken);
                return false;
            }
            return accessTokenRepository.findByAccessToken(accessToken)
                            .map(t -> !t.isRevoked())
                            .orElse(false);
        } catch (ExpiredJwtException e) {
            LOGGER.error("Expired JwtException", e);
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Unsupported JwtException", e);
        } catch (MalformedJwtException e) {
            LOGGER.error("Malformed JwtException", e);
        } catch (SecurityException e) {
            LOGGER.error("Security Exception", e);
        } catch (Exception e) {
            LOGGER.error("Invalid token", e);
        }
        return false;
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            if (isTokenExpired(refreshToken)) {
                revokeRefreshToken(refreshToken);
                return false;
            }
            return refreshTokenRepository.findByRefreshToken(refreshToken)
                            .map(t -> !t.isRevoked())
                            .orElse(false);
        } catch (ExpiredJwtException e) {
            LOGGER.error("Expired JwtException", e);
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Unsupported JwtException", e);
        } catch (MalformedJwtException e) {
            LOGGER.error("Malformed JwtException", e);
        } catch (SecurityException e) {
            LOGGER.error("Security Exception", e);
        } catch (Exception e) {
            LOGGER.error("Invalid token", e);
        }
        return false;
    }

    public String extractUsername(String token) {
        return parseToken(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return parseToken(token).getExpiration();
    }

    public long getRefreshTokenExpirationSeconds() {
        return (long) refreshTokenExpirationDays * 24 * 60 * 60;
    }

    public void saveTokens(String login, String accessToken, String refreshToken) {
        Instant accessExpiry = extractExpiration(accessToken).toInstant();
        Instant refreshExpiry = extractExpiration(refreshToken).toInstant();

        AccessToken userAccessToken = new AccessToken();
        userAccessToken.setUsername(login);
        userAccessToken.setAccessToken(accessToken);
        userAccessToken.setAccessTokenExpiry(accessExpiry);

        RefreshToken userRefreshToken = new RefreshToken();
        userRefreshToken.setUsername(login);
        userRefreshToken.setRefreshToken(refreshToken);
        userRefreshToken.setRefreshTokenExpiry(refreshExpiry);

        accessTokenRepository.save(userAccessToken);
        refreshTokenRepository.save(userRefreshToken);
    }

    public void saveRefreshAccessToken(String login, String accessToken) {
        Instant accessExpiry = extractExpiration(accessToken).toInstant();

        AccessToken userAccessToken = new AccessToken();
        userAccessToken.setUsername(login);
        userAccessToken.setAccessToken(accessToken);
        userAccessToken.setAccessTokenExpiry(accessExpiry);

        accessTokenRepository.save(userAccessToken);
    }

    public void revokeAllTokens(String accessToken, String refreshToken) {
        revokeAccessToken(accessToken);
        revokeRefreshToken(refreshToken);
    }

    public void revokeLatestAccessTokensForUser(String username) {
        accessTokenRepository.findLatestByUsername(username)
                .ifPresent(accessToken -> {
                    accessToken.setRevoked(true);
                    accessTokenRepository.save(accessToken);
                });
    }

    public void revokeAccessToken(String token) {
        accessTokenRepository.findByAccessToken(token)
                .ifPresent(accessToken -> {
                    accessToken.setRevoked(true);
                    accessTokenRepository.save(accessToken);
                });
    }

    public void revokeRefreshToken(String token) {
        refreshTokenRepository.findByRefreshToken(token)
                .ifPresent(refreshToken -> {
                    refreshToken.setRevoked(true);
                    refreshTokenRepository.save(refreshToken);
                });
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
