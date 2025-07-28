package ashteam.farm_leftover.product.service;

import ashteam.farm_leftover.product.dto.FarmProductDto;
import ashteam.farm_leftover.product.dto.NewProductDto;
import ashteam.farm_leftover.product.dto.ProductDto;

public interface ProductService {
    FarmProductDto addProduct(String farmId, NewProductDto newProductDto);

    FarmProductDto updateProductById(String productId, NewProductDto newProductDto, String farmId);

    void deleteProduct(String productId, String farmId);

    Iterable<FarmProductDto> findProductsByFarmId(String farmId);

    Iterable<ProductDto> findAllProducts(String sort);

    Iterable<ProductDto> searchProducts(String query);
}
