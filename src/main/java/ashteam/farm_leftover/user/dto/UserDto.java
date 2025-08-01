package ashteam.farm_leftover.user.dto;

import ashteam.farm_leftover.user.model.Role;
import lombok.Getter;

import java.util.Set;

@Getter
public class UserDto {
    String userName;
    String email;
    String phone;
    String farmName;
    String city;
    String street;
}
