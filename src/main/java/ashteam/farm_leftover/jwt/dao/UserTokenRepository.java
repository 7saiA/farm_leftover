package ashteam.farm_leftover.jwt.dao;

import ashteam.farm_leftover.jwt.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, String> {
    Optional<UserToken> findByAccessToken(String accessToken);
    Optional<UserToken> findByRefreshToken(String refreshToken);
    UserToken findByUsername(String username);
    void deleteByUsername(String username);
}
