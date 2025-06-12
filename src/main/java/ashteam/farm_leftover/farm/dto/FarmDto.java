package ashteam.farm_leftover.farm.dto;

import ashteam.farm_leftover.product.model.Product;
import lombok.Getter;

import java.util.Set;

@Getter
public class FarmDto {
    Integer farmId;
    String farmName;
    String email;
    String password;
    String city;
    String street;
    String phone;
    Set<Product> products;
}
