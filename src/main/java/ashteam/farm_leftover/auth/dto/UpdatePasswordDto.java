package ashteam.farm_leftover.auth.dto;

import lombok.Data;

@Data
public class UpdatePasswordDto {
    private String oldPassword;
    private String newPassword;
    private String accessToken;
    private String refreshToken;
}
