package ashteam.farm_leftover.user.dto;

import ashteam.farm_leftover.product.model.Product;
import ashteam.farm_leftover.user.model.RoleUser;
import lombok.Getter;

import java.util.Set;

@Getter
public class NewUserDto {
    String name;
    String email;
    String password;
    String phone;
    RoleUser roleUser;
    String city;
    String street;
    Set<Product> products;
}
