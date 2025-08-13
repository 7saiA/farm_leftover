package ashteam.farm_leftover.product.service;

import ashteam.farm_leftover.product.dao.ProductRepository;
import ashteam.farm_leftover.product.dto.FarmProductDto;
import ashteam.farm_leftover.product.dto.NewProductDto;
import ashteam.farm_leftover.product.dto.ProductDto;
import ashteam.farm_leftover.product.dto.exceptions.ProductNotFoundException;
import ashteam.farm_leftover.product.model.Product;
import ashteam.farm_leftover.user.dao.UserAccountRepository;
import ashteam.farm_leftover.user.dto.exceptions.UserNotFoundException;
import ashteam.farm_leftover.user.model.Role;
import ashteam.farm_leftover.user.model.UserAccount;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    final ProductRepository productRepository;
    final UserAccountRepository userAccountRepository;
    final ModelMapper modelMapper;
    final Cloudinary cloudinary;

    @Transactional
    @Override
    public FarmProductDto addProduct(String farmId, String newProductJson, MultipartFile file) {
        UserAccount farm = userAccountRepository.findById(farmId)
                .orElseThrow(() -> new UserNotFoundException(farmId));
        if (!farm.getRole().equals(Role.FARM)) {
            throw new IllegalArgumentException();
        }
        NewProductDto newProductDto = parseJson(newProductJson);

        if (newProductDto.getProductName() == null
        || newProductDto.getPricePerUnit() == null
        || newProductDto.getUnit() == null
        || newProductDto.getAvailableQuantity() == null
        || newProductDto.getAvailableQuantity() == 0) {
            throw new IllegalArgumentException();
        }
        Product product = new Product(
                newProductDto.getProductName(),
                newProductDto.getPricePerUnit(),
                newProductDto.getUnit(),
                newProductDto.getAvailableQuantity()
        );
        String imageUrl = uploadImage(file);
        if(imageUrl != null){
            product.setImgUrl(imageUrl);
        }
        farm.addProduct(product);
        productRepository.save(product);
        return modelMapper.map(product, FarmProductDto.class);
    }

    @Transactional
    @Override
    public FarmProductDto updateProductById(String productId, String newProductJson, String farmId,MultipartFile file) {
        UserAccount farm = userAccountRepository.findById(farmId)
                .orElseThrow(() -> new UserNotFoundException(farmId));
        if (!farm.getRole().equals(Role.FARM)) {
            throw new IllegalArgumentException();
        }
        NewProductDto newProductDto = parseJson(newProductJson);
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
        String imageUrl = uploadImage(file);
        if(imageUrl != null){
            product.setImgUrl(imageUrl);
        }
        product = productRepository.save(product);
        return modelMapper.map(product, FarmProductDto.class);
    }

    @Transactional
    @Override
    public void deleteProduct(String productId, String farmId) {
        UserAccount farm = userAccountRepository.findById(farmId)
                .orElseThrow(() -> new UserNotFoundException(farmId));
        if (!farm.getRole().equals(Role.FARM)) {
            throw new IllegalArgumentException();
        }
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        if (farm != product.getUserAccount()) {
            throw new IllegalArgumentException();
        }
        productRepository.deleteById(productId);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<FarmProductDto> findProductsByFarmId(String farmId) {
        return productRepository.findAllByUserAccountLogin(farmId)
                .stream()
                .map(p -> modelMapper.map(p, FarmProductDto.class))
                .toList();
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

    @Override
    @Transactional
    public void uploadProductImage(String login, String productId, MultipartFile file) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (!product.getUserAccount().getLogin().equals(login)) {
            throw new AccessDeniedException("You cannot upload an image for this product");
        }

        String imageUrl = uploadImage(file);
        if(imageUrl != null){
            product.setImgUrl(imageUrl);
            productRepository.save(product);
        }
    }

    private NewProductDto parseJson(String newProductJson){
        NewProductDto newProductDto;
        try {
            ObjectMapper mapper = new ObjectMapper();
            newProductDto = mapper.readValue(newProductJson, NewProductDto.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON for newProduct", e);
        }
        return newProductDto;
    }

    private String uploadImage(MultipartFile file){
        if(file == null || file.isEmpty()){
            return null;
        }
        try {
            Map<String,Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "products",
                            "use_filename", true,
                            "unique_filename", false,
                            "overwrite", true
                    )
            );
            return (String) uploadResult.get("secure_url");
        }catch (Exception e){
            throw new RuntimeException("failed to upload product image",e);
        }
    }
}
