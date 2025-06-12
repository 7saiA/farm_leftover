package ashteam.farm_leftover.farm.dto;

import lombok.Getter;

@Getter
public class FarmUpdatePasswordDto {
    String oldPassword;
    String newPassword;
}
