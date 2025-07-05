package ashteam.farm_leftover.product.service;

import ashteam.farm_leftover.product.dto.NewProductDto;
import ashteam.farm_leftover.product.dto.ProductDto;

public interface ProductService {
    ProductDto addProduct(String farmId, NewProductDto newProductDto);

    ProductDto findProductByName(Long productId);

    ProductDto updateProductById(Long productId, NewProductDto newProductDto);

    ProductDto deleteProduct(Long productId);

    Iterable<ProductDto> findAllProducts(String sort);

    Iterable<ProductDto> findProductsByFarm(String name);
}
