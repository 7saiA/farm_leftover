package ashteam.farm_leftover.product.dto;

import ashteam.farm_leftover.user.dto.FarmForSearchDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchResultDto {
    private Iterable<FarmForSearchDto> farms;
    private Iterable<ProductDto> products;
}
