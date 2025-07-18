package ashteam.farm_leftover.product.service;

import ashteam.farm_leftover.product.dto.NewProductDto;
import ashteam.farm_leftover.product.dto.ProductDto;

public interface ProductService {
    ProductDto addProduct(String farmId, NewProductDto newProductDto);

    ProductDto updateProductById(Long productId, NewProductDto newProductDto, String farmId);

    ProductDto deleteProduct(Long productId, String farmId);

    Iterable<ProductDto> findProductsByFarmId(String farmId);

    ProductDto findProductByName(Long productId);

    Iterable<ProductDto> findAllProducts(String sort);
}
