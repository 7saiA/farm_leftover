package ashteam.farm_leftover.product.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class FarmProductDto {
    String productId;
    String productName;
    BigDecimal pricePerUnit;
    String unit;
    Integer availableQuantity;
    String imgUrl;
}
