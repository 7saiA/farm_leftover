package ashteam.farm_leftover.product.service;

import ashteam.farm_leftover.product.dto.NewProductDto;
import ashteam.farm_leftover.product.dto.ProductDto;

public interface ProductService {
    ProductDto addProduct(Integer farmId, NewProductDto newProductDto);

    ProductDto findProductByName(Integer productId);

    ProductDto updateProductById(Integer productId, NewProductDto newProductDto);

    ProductDto deleteProduct(Integer productId);

    Iterable<ProductDto> findAllProducts();
}
