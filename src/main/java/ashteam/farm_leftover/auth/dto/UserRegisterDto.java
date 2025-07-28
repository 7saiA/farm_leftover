package ashteam.farm_leftover.auth.dto;

import ashteam.farm_leftover.user.model.Role;
import lombok.Getter;

@Getter
public class UserRegisterDto {
    String login;
    String userName;
    String email;
    String password;
    String phone;
    String farmName;
    String city;
    String street;
}
