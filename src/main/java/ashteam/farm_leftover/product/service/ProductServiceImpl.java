package ashteam.farm_leftover.product.service;

import ashteam.farm_leftover.product.dao.ProductRepository;
import ashteam.farm_leftover.product.dto.NewProductDto;
import ashteam.farm_leftover.product.dto.ProductDto;
import ashteam.farm_leftover.product.dto.exceptions.ProductNotFoundException;
import ashteam.farm_leftover.product.model.Product;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    final ProductRepository productRepository;
    final ModelMapper modelMapper;

    @Override
    public ProductDto addProduct(String farmId, NewProductDto newProductDto) {
        Product product = new Product(farmId, newProductDto.getProductName(), newProductDto.getPricePerUnit(), newProductDto.getUnit(), newProductDto.getAvailableQuantity());
        product = productRepository.save(product);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto findProductByName(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto updateProductById(String productId, NewProductDto newProductDto) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        if(newProductDto.getProductName() != null){
            product.setProductName(newProductDto.getProductName());
        }
        if(newProductDto.getPricePerUnit() != null){
            product.setPricePerUnit(newProductDto.getPricePerUnit());
        }
        if(newProductDto.getUnit() != null){
            product.setUnit(newProductDto.getUnit());
        }
        if(newProductDto.getAvailableQuantity() != null){
            product.setAvailableQuantity(newProductDto.getAvailableQuantity());
        }
        product = productRepository.save(product);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto deleteProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        productRepository.deleteById(productId);
        return modelMapper.map(product, ProductDto.class);
    }
}
