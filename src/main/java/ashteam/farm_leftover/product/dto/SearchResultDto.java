package ashteam.farm_leftover.product.dto;

import ashteam.farm_leftover.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchResultDto {
    private Iterable<UserDto> farms;
    private Iterable<ProductDto> products;
}
