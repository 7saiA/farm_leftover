package ashteam.farm_leftover.product.service;

import ashteam.farm_leftover.product.dto.NewProductDto;
import ashteam.farm_leftover.product.dto.ProductDto;

public interface ProductService {
    ProductDto addProduct(String farmId, NewProductDto newProductDto);

    ProductDto findProductByName(String productId);

    ProductDto updateProductById(String productId, NewProductDto newProductDto);

    ProductDto deleteProduct(String productId);

    Iterable<ProductDto> findAllProducts();
}
