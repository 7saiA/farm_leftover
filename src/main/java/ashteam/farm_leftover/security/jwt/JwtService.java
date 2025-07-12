package ashteam.farm_leftover.security.jwt;

import ashteam.farm_leftover.security.dto.JwtAuthenticationDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtService {

    //this interface is API log, witch defines methods for writing logs (these are records of events in the program)
    private static final Logger LOGGER = LogManager.getLogger(JwtService.class);
    private static final String jwtSecret = "cb651aed68818096aaa79e3bbdce859843e0da93ea916e8c88ecc526b4c7333abc9bc83d5a617481c04b9e5df8fbed29";

    //ConcurrentHashMap - thread-safe Map
    private final Map<String, Date> tokenBlackList = new ConcurrentHashMap<>();

    public JwtAuthenticationDto generateAuthToken(String login) {
        //UUID - this is a 128-bit unique identifier that guarantees virtually zero chance of duplication.
        String accessTokenId = UUID.randomUUID().toString();
        String refreshTokenId = UUID.randomUUID().toString();

        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(login, accessTokenId));
        jwtDto.setRefreshToken(generateJwtRefreshToken(login, refreshTokenId));
        return jwtDto;
    }

    public JwtAuthenticationDto refreshAccessToken(String login, String refreshToken) {
        Claims refreshClaims = parseToken(refreshToken);
        String refreshTokenId = refreshClaims.getId();
        if (isTokenRevoked(refreshTokenId)) {
            throw new JwtException("Refresh token was revoked");
        }
        String newAccessTokenId = UUID.randomUUID().toString();

        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(login, newAccessTokenId));
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }

    public String getLoginFromToken(String token) {
        return parseToken(token).getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Claims claims = parseToken(token);
            String tokenId = claims.getId();
            if (isTokenRevoked(tokenId)) {
                //tokenId will replace {}
                LOGGER.warn("Attempt to use revoked token: {}", tokenId);
                return false;
            }
            return true;
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

    public void revokeAllTokens(String accessToken, String refreshToken) {
        try {
            Claims accessClaims = parseToken(accessToken);
            tokenBlackList.put(accessClaims.getId(), accessClaims.getExpiration());

            Claims refreshClaims = parseToken(refreshToken);
            tokenBlackList.put(refreshClaims.getId(), refreshClaims.getExpiration());

            LOGGER.info("All tokens revoked for user: {}", refreshClaims.getSubject());
        } catch (Exception e) {
            LOGGER.error("Failed to revoke tokens", e);
        }
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenRevoked(String tokenId) {
        return tokenBlackList.containsKey(tokenId);
    }

    private String generateJwtToken(String login, String tokenId) {
        Date date = Date.from(LocalDateTime.now()
                .plusMinutes(15)
                .atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                //Add tokenId
                .id(tokenId)
                .subject(login)
                .expiration(date)
                .signWith(getSignKey())
                .compact();
    }

    private String generateJwtRefreshToken(String login, String tokenId) {
        Date date = Date.from(LocalDateTime.now()
                .plusDays(7)
                .atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                //Add tokenId
                .id(tokenId)
                .subject(login)
                .expiration(date)
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Scheduled(fixedRate = 3600000)
    private void cleanupTokenBlackList() {
        Date now = new Date();

        tokenBlackList.entrySet()
                .removeIf(entry -> {
                    boolean expired = entry.getValue().before(now);
                    if (expired) {
                        LOGGER.debug("Removing expired token: {}", entry.getKey());
                    }
                    return expired;
                });
    }
}