package ashteam.farm_leftover.jwt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "access_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String tokenId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private Instant accessTokenExpiry;

    @Column(nullable = false)
    private boolean revoked = false;
}
