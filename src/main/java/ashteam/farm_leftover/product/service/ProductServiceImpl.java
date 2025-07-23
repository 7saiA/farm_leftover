package ashteam.farm_leftover.product.service;

import ashteam.farm_leftover.product.dao.ProductRepository;
import ashteam.farm_leftover.product.dto.NewProductDto;
import ashteam.farm_leftover.product.dto.ProductDto;
import ashteam.farm_leftover.product.dto.exceptions.ProductNotFoundException;
import ashteam.farm_leftover.product.model.Product;
import ashteam.farm_leftover.user.dao.UserAccountRepository;
import ashteam.farm_leftover.user.dto.exceptions.UserNotFoundException;
import ashteam.farm_leftover.user.model.Role;
import ashteam.farm_leftover.user.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    final ProductRepository productRepository;
    final UserAccountRepository userAccountRepository;
    final ModelMapper modelMapper;

    @Transactional
    @Override
    public ProductDto addProduct(String farmId, NewProductDto newProductDto) {
        UserAccount farm = userAccountRepository.findById(farmId)
                .orElseThrow(() -> new UserNotFoundException(farmId));
        if (!farm.getRole().equals(Role.FARM)) {
            throw new IllegalArgumentException();
        }
        Product product = new Product(
                newProductDto.getProductName(),
                newProductDto.getPricePerUnit(),
                newProductDto.getUnit(),
                newProductDto.getAvailableQuantity()
        );
        farm.addProduct(product);
        productRepository.save(product);
        return modelMapper.map(product, ProductDto.class);
    }

    @Transactional
    @Override
    public ProductDto updateProductById(Long productId, NewProductDto newProductDto, String farmId) {
        UserAccount farm = userAccountRepository.findById(farmId)
                .orElseThrow(() -> new UserNotFoundException(farmId));
        if (!farm.getRole().equals(Role.FARM)) {
            throw new IllegalArgumentException();
        }
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
        if (!farm.getProducts().contains(product)) {
            throw new IllegalArgumentException();
        }
        product = productRepository.save(product);
        return modelMapper.map(product, ProductDto.class);
    }

    @Transactional
    @Override
    public ProductDto deleteProduct(Long productId, String farmId) {
        UserAccount farm = userAccountRepository.findById(farmId)
                .orElseThrow(() -> new UserNotFoundException(farmId));
        if (!farm.getRole().equals(Role.FARM)) {
            throw new IllegalArgumentException();
        }
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        if (!farm.getProducts().contains(product)) {
            throw new IllegalArgumentException();
        }
        productRepository.deleteById(productId);
        return modelMapper.map(product, ProductDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<ProductDto> findProductsByFarmId(String farmId) {
        return productRepository.findAllByUserAccountLogin(farmId)
                .stream()
                .map(p -> modelMapper.map(p, ProductDto.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public ProductDto findProductByName(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        return modelMapper.map(product, ProductDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<ProductDto> findAllProducts(String sortOption) {
        Sort sort = switch (sortOption.toLowerCase()) {
            case "a-z" -> Sort.by(Sort.Direction.ASC, "productName");
            case "z-a" -> Sort.by(Sort.Direction.DESC, "productName");
            case "price-low-high" -> Sort.by(Sort.Direction.ASC, "pricePerUnit");
            case "price-high-low" -> Sort.by(Sort.Direction.DESC, "pricePerUnit");
            case "newest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            default -> Sort.unsorted();
        };

        List<Product> products = productRepository.findAll(sort);

        return products.stream()
                .map(p -> modelMapper.map(p, ProductDto.class))
                .toList();
    }

    @Override
    public Iterable<ProductDto> searchProducts(String query) {
        return productRepository.findByProductNameContainingIgnoreCase(query)
                .stream()
                .map(p -> modelMapper.map(p, ProductDto.class))
                .toList();
    }
}
