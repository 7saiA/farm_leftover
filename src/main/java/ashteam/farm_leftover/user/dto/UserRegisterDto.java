package ashteam.farm_leftover.user.dto;

import ashteam.farm_leftover.user.model.Role;
import lombok.Getter;

import java.util.Set;

@Getter
public class UserRegisterDto {
    String login;
    String email;
    String password;
    String phone;
    Set<Role> roles;
    String farmName;
    String city;
    String street;
}
