package ashteam.farm_leftover.product.dto;

import lombok.Getter;

@Getter
public class ProductDto {
    String productId;
    String farmId;
    String productName;
    Double pricePerUnit;
    String unit;
    Integer availableQuantity;
}
