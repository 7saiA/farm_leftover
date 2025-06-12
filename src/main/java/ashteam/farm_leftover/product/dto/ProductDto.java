package ashteam.farm_leftover.product.dto;

import ashteam.farm_leftover.user.dto.UserForProductDto;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ProductDto {
    Long productId;
    String productName;
    Double pricePerUnit;
    String unit;
    Integer availableQuantity;
    @Setter
    UserForProductDto userForProductDto;
}
