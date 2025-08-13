package ashteam.farm_leftover.product.service;

import ashteam.farm_leftover.product.dto.FarmProductDto;
import ashteam.farm_leftover.product.dto.NewProductDto;
import ashteam.farm_leftover.product.dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    FarmProductDto addProduct(String farmId, String newProductJson,MultipartFile file);

    FarmProductDto updateProductById(String productId, String newProductJson, String farmId, MultipartFile file);

    void deleteProduct(String productId, String farmId);

    Iterable<FarmProductDto> findProductsByFarmId(String farmId);

    Iterable<ProductDto> findAllProducts(String sort);

    Iterable<ProductDto> searchProducts(String query);

    void uploadProductImage(String login, String productId, MultipartFile file);
}
