package ashteam.farm_leftover.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    private SecretKey accessToken;
    private SecretKey refreshToken;

    @PostConstruct
    public void initKeys(){
        accessToken = Keys.hmacShaKeyFor("access-secret-key-32bytesminimum!".getBytes());
        refreshToken = Keys.hmacShaKeyFor("refresh-secret-key-32bytesminimum!".getBytes());
    }

    public String generateAccessToken(String login){
        return Jwts.builder()
                .claim("sub", login)
                .claim("iat", new Date())
                .claim("exp", new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .signWith(accessToken)
                .compact();
    }

    public String generateRefreshToken(String login){
        return Jwts.builder()
                .claim("sub", login)
                .claim("iat", new Date())
                .claim("exp", new Date(System.currentTimeMillis() + 7 * 24 * 60 * 1000))
                .signWith(refreshToken)
                .compact();
    }

    public String extractUserName(String token){
        return Jwts.parser()
                .verifyWith(accessToken)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("sub", String.class);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        try {
            String username = extractUserName(token);
            return username.equals(userDetails.getUsername());
        }catch (Exception e){
            return false;
        }
    }
}
