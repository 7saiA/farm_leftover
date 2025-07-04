package ashteam.farm_leftover.product.dto;

import lombok.Getter;

@Getter
public class ProductForFarmDto {
    Long productId;
    String productName;
    Double pricePerUnit;
    String unit;
    Integer availableQuantity;
}
