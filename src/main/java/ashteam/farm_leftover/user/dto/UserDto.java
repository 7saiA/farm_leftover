package ashteam.farm_leftover.user.dto;

import ashteam.farm_leftover.product.dto.ProductForFarmDto;
import ashteam.farm_leftover.user.model.Role;
import lombok.Getter;

import java.util.Set;

@Getter
public class UserDto {
    String login;
    String email;
    String password;
    String phone;
    Role role;
    String farmName;
    String city;
    String street;
    Set<ProductForFarmDto> products;
}
