package ashteam.farm_leftover.customer.dto;

import lombok.Getter;

@Getter
public class CustomerDto {
    Integer customerId;
    String customerName;
    String email;
    String password;
    String phone;
}
