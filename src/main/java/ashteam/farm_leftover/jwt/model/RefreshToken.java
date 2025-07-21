package ashteam.farm_leftover.jwt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "refresh_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String tokenId;

    @Column(nullable = false)
    private String username;

    @Column
    private String refreshToken;

    @Column
    private Instant refreshTokenExpiry;

    @Column(nullable = false)
    private boolean revoked = false;
}
