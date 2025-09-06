package ashteam.farm_leftover.paypal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayPalUrlsDto {
    String cancelUrl;
    String successUrl;
}
