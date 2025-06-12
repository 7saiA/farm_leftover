package ashteam.farm_leftover.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForProductDto {
    Long id;
    String name;
    String email;
    String phone;
    String city;
    String street;
}
