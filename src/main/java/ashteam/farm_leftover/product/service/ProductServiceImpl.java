package ashteam.farm_leftover.product.service;

import ashteam.farm_leftover.product.dao.ProductRepository;
import ashteam.farm_leftover.product.dto.NewProductDto;
import ashteam.farm_leftover.product.dto.ProductDto;
import ashteam.farm_leftover.product.dto.exceptions.ProductNotFoundException;
import ashteam.farm_leftover.product.model.Product;
import ashteam.farm_leftover.user.dao.UserRepository;
import ashteam.farm_leftover.user.dto.exceptions.UserNotFoundException;
import ashteam.farm_leftover.user.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    final ProductRepository productRepository;
    final UserRepository userRepository;
    final ModelMapper modelMapper;

    @Transactional
    @Override
    public ProductDto addProduct(String farmId, NewProductDto newProductDto) {
        UserAccount user = userRepository.findById(farmId)
                .orElseThrow(() -> new UserNotFoundException(farmId));

        Product product = new Product(
                newProductDto.getProductName(),
                newProductDto.getPricePerUnit(),
                newProductDto.getUnit(),
                newProductDto.getAvailableQuantity()
        );

        user.addProduct(product);
        productRepository.save(product);

        return modelMapper.map(product, ProductDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductDto findProductByName(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        return modelMapper.map(product, ProductDto.class);
    }

    @Transactional
    @Override
    public ProductDto updateProductById(Long productId, NewProductDto newProductDto) {
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

    @Transactional
    @Override
    public ProductDto deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        productRepository.deleteById(productId);
        return modelMapper.map(product, ProductDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<ProductDto> findAllProducts() {
        return productRepository.findAll().stream()
                .map(p -> modelMapper.map(p, ProductDto.class))
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<ProductDto> findProductsByFarm(String name) {
        return productRepository.findAllByUserAccountLogin(name)
                .stream()
                .map(p -> modelMapper.map(p, ProductDto.class))
                .toList();
    }
}
