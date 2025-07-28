package ashteam.farm_leftover.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
public class ProductDto {
    String productId;
    String productName;
    BigDecimal pricePerUnit;
    String unit;
    Integer availableQuantity;
    @Setter
    String farmName;
}
