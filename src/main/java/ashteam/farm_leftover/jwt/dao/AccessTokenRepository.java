package ashteam.farm_leftover.jwt.dao;

import ashteam.farm_leftover.jwt.model.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, String> {
    Optional<AccessToken> findByAccessToken(String accessToken);
    AccessToken findByUsername(String username);
    @Query("SELECT a FROM AccessToken a WHERE a.username = :username ORDER BY a.accessTokenExpiry DESC LIMIT 1")
    Optional<AccessToken> findLatestByUsername(@Param("username") String username);
    void deleteByUsername(String username);
}
