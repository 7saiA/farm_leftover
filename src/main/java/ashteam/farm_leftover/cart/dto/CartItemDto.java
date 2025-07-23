package ashteam.farm_leftover.cart.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CartItemDto {
    Long productId;
    String productName;
    BigDecimal pricePerUnit;
    String unit;
    int quantity;
    BigDecimal subtotal;
}
