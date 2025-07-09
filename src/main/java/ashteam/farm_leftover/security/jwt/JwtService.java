package ashteam.farm_leftover.security.jwt;

import ashteam.farm_leftover.auth.dto.JwtAuthenticationDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtService {

    //this interface is API log, witch defines methods for writing logs (these are records of events in the program)
    private static final Logger LOGGER = LogManager.getLogger(JwtService.class);
    private static final String jwtSecret = "cb651aed68818096aaa79e3bbdce859843e0da93ea916e8c88ecc526b4c7333abc9bc83d5a617481c04b9e5df8fbed29";

    public JwtAuthenticationDto generateAuthToken(String login){
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(login));
        jwtDto.setRefreshToken(generateJwtRefreshToken(login));
        return jwtDto;
    }

    public JwtAuthenticationDto refreshAccessToken(String login, String refreshToken){
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(login));
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }

    public String getLoginFromToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateJwtToken(String token){
        try {
            Jwts.parser()
//                    .require("alg", "HS256")
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        }catch (ExpiredJwtException e){
            LOGGER.error("Expired JwtException",e);
        }catch (UnsupportedJwtException e){
            LOGGER.error("Unsupported JwtException",e);
        }catch (MalformedJwtException e){
            LOGGER.error("Malformed JwtException",e);
        }catch (SecurityException e){
            LOGGER.error("Security Exception",e);
        }catch (Exception e){
            LOGGER.error("Invalid token",e);
        }
        return false;
    }

    private String generateJwtToken(String login) {
        Date date = Date.from(LocalDateTime.now().plusMinutes(15).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(login)
                .expiration(date)
                .signWith(getSignKey())
                .compact();
    }

    private String generateJwtRefreshToken(String login) {
        Date date = Date.from(LocalDateTime.now().plusDays(7).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(login)
                .expiration(date)
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
