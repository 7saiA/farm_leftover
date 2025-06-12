package ashteam.farm_leftover.customer.dto;

import lombok.Getter;

@Getter
public class CustomerUpdatePasswordDto {
    String oldPassword;
    String newPassword;
}
