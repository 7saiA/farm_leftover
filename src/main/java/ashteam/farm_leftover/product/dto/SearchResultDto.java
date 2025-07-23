package ashteam.farm_leftover.product.dto;

import ashteam.farm_leftover.user.dto.FarmDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchResultDto {
    private Iterable<FarmDto> farms;
    private Iterable<ProductDto> products;
}
