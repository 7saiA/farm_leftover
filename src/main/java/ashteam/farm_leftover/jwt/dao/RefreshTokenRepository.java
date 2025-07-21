package ashteam.farm_leftover.jwt.dao;

import ashteam.farm_leftover.jwt.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    RefreshToken findByUsername(String username);
    void deleteByUsername(String username);

}
