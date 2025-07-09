package ashteam.farm_leftover.auth.dto;

import ashteam.farm_leftover.user.model.Role;
import lombok.Getter;

@Getter
public class UserRegisterDto {
    String login;
    String email;
    String password;
    String phone;
    Role role;
    String farmName;
    String city;
    String street;
}
