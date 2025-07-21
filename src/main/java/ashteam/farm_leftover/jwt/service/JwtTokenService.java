package ashteam.farm_leftover.jwt.service;

import ashteam.farm_leftover.jwt.dao.UserTokenRepository;
import ashteam.farm_leftover.jwt.model.UserToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenService {

//    @Value("${jwt.secret}")
    private static final String secret = "25bf957ee14c22b0b22711ff49b34a705227433040c82c4112f6e6dbdc443746";
    private static final int accessTokenExpirationMinutes = 15;
    private static final int refreshTokenExpirationDays = 7;

    private final UserTokenRepository userTokenRepository;

    public String generateAccessToken(String login) {
        return buildToken(login, accessTokenExpirationMinutes, ChronoUnit.MINUTES);
    }

    public String generateRefreshToken(String login) {
        return buildToken(login, refreshTokenExpirationDays, ChronoUnit.DAYS);
    }

    private String buildToken(String login, long expiration, ChronoUnit unit) {
        Instant now = Instant.now();
        Instant expiry = now.plus(expiration, unit);

        return Jwts.builder()
                .subject(login)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(getSignKey())
                .compact();
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            return !isTokenExpired(refreshToken) &&
                    userTokenRepository.findByRefreshToken(refreshToken)
                            .map(t -> !t.isRevoked())
                            .orElse(false);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            return !isTokenExpired(accessToken) &&
                    userTokenRepository.findByAccessToken(accessToken)
                            .map(t -> !t.isRevoked())
                            .orElse(false);
        } catch (Exception e) {
            return false;
        }
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

    public void saveUserToken(String login, String accessToken, String refreshToken) {
        Instant accessExpiry = extractExpiration(accessToken).toInstant();
        Instant refreshExpiry = refreshToken != null ? extractExpiration(refreshToken).toInstant() : null;

        userTokenRepository.deleteByUsername(login);

        UserToken userToken = new UserToken();
        userToken.setUsername(login);
        userToken.setAccessToken(accessToken);
        userToken.setRefreshToken(refreshToken);
        userToken.setAccessTokenExpiry(accessExpiry);
        userToken.setRefreshTokenExpiry(refreshExpiry);

        userTokenRepository.save(userToken);
    }

    public void revokeAllTokens(String login) {
        UserToken userToken = userTokenRepository.findByUsername(login);
        userTokenRepository.delete(userToken);
    }

    public void revokeAccessToken(String token) {
        userTokenRepository.findByAccessToken(token)
                .ifPresent(userToken -> {
                    userToken.setRevoked(true);
                    userTokenRepository.save(userToken);
                });
    }

    public void revokeRefreshToken(String token) {
        userTokenRepository.findByRefreshToken(token)
                .ifPresent(userToken -> {
                    userToken.setRevoked(true);
                    userTokenRepository.save(userToken);
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
