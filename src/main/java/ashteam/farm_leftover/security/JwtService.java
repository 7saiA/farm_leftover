package ashteam.farm_leftover.security;

import ashteam.farm_leftover.user.dto.exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {
    private SecretKey accessTokenKey;
    private SecretKey refreshTokenKey;

    @PostConstruct
    public void initKeys(){
        accessTokenKey = Keys.hmacShaKeyFor("r8dX4vLmT9kCw1zPqHsE6ByGfAeUbMn2".getBytes());
        refreshTokenKey = Keys.hmacShaKeyFor("hJqV7nXyPz4CfLmR3eWoBtKiGdUuAxNs".getBytes());
    }

    public String generateAccessToken(String login) {
        return Jwts.builder()
                .claim("sub", login)
                .claim("iat", System.currentTimeMillis() / 1000)  // issued at (seconds)
                .claim("exp", (System.currentTimeMillis() / 1000) + 15 * 60)  // expires in 10 seconds
                .signWith(accessTokenKey)
                .compact();
    }

    public String generateRefreshToken(String login) {
        return Jwts.builder()
                .claim("sub", login)
                .claim("iat", System.currentTimeMillis() / 1000)
                .claim("exp", (System.currentTimeMillis() / 1000) + 7L * 24 * 60 * 60)  // 7 days
                .signWith(refreshTokenKey)
                .compact();
    }

    public String extractUsernameFromAccessToken(String token) {
        return extractUserName(token, accessTokenKey);
    }

    public String extractUsernameFromRefreshToken(String token) {
        return extractUserName(token, refreshTokenKey);
    }

    public String extractUserName(String token, SecretKey key) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Long exp = claims.get("exp", Long.class);
            long now = System.currentTimeMillis() / 1000;

            if (exp != null && exp < now) {
                throw new InvalidTokenException("Token expired");
            }

            return claims.get("sub", String.class);
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid or expired token");
        }
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        return isTokenValid(token, userDetails, accessTokenKey);
    }

    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        return isTokenValid(token, userDetails, refreshTokenKey);
    }

    public boolean isTokenValid(String token, UserDetails userDetails, SecretKey key){
        try {
            String username = extractUserName(token,key);
            return username.equals(userDetails.getUsername());
        }catch (Exception e){
            return false;
        }
    }
}
