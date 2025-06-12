package ashteam.farm_leftover.product.dto;

import lombok.Getter;

@Getter
public class ProductDto {
    Integer productId;
    Integer farmId;
    String productName;
    Double pricePerUnit;
    String unit;
    Integer availableQuantity;
}
