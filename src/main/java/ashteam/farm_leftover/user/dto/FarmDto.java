package ashteam.farm_leftover.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
public class FarmDto {
    String farmName;
    String email;
    String phone;
    String city;
    String street;
    @Setter
    Set<ProductForFarmDto> products;
}
