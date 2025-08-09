package ashteam.farm_leftover.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    String productId;
    String productName;
    Integer quantity;
    String unit;
    BigDecimal pricePerUnit;
    BigDecimal subtotal;
}
