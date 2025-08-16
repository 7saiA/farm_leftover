package ashteam.farm_leftover.user.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ProductForFarmDto {
    String productId;
    String productName;
    BigDecimal pricePerUnit;
    String unit;
    Integer availableQuantity;
    String imgUrl;
}
