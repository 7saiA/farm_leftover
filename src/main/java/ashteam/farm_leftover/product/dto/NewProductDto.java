package ashteam.farm_leftover.product.dto;

import lombok.Getter;

@Getter
public class NewProductDto {
    String productName;
    Double pricePerUnit;
    String unit;
    Integer availableQuantity;
}
