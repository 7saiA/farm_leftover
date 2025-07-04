package ashteam.farm_leftover.user.dto;

import ashteam.farm_leftover.product.dto.ProductForFarmDto;
import lombok.Getter;

import java.util.Set;

@Getter
public class FarmDto {
    String login;
    String email;
    String phone;
    String farmName;
    String city;
    String street;
    Set<ProductForFarmDto> products;
}
