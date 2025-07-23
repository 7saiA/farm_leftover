package ashteam.farm_leftover.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
public class CartResponseDto {
    Long cartId;
    List<CartItemDto> items;
    BigDecimal totalPrice;
}
