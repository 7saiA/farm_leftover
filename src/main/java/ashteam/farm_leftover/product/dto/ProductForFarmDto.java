package ashteam.farm_leftover.product.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ProductForFarmDto {
    Long productId;
    String productName;
    BigDecimal pricePerUnit;
    String unit;
    Integer availableQuantity;
    LocalDateTime createdAt;
}
