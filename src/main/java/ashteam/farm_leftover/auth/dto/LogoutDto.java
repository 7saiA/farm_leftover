package ashteam.farm_leftover.auth.dto;

import lombok.Data;

@Data
public class LogoutDto {
    private String accessToken;
    private String refreshToken;
}
