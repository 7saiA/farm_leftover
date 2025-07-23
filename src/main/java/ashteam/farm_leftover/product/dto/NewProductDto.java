package ashteam.farm_leftover.product.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class NewProductDto {
    String productName;
    BigDecimal pricePerUnit;
    String unit;
    Integer availableQuantity;
}
