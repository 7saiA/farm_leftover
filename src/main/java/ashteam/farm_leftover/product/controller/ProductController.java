package ashteam.farm_leftover.product.controller;

import ashteam.farm_leftover.product.dto.NewProductDto;
import ashteam.farm_leftover.product.dto.ProductDto;
import ashteam.farm_leftover.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    final ProductService productService;

    @PostMapping("/{farmId}")
    public ProductDto addProduct(@PathVariable String farmId, @RequestBody NewProductDto newProductDto) {
        return productService.addProduct(farmId, newProductDto);
    }

    @GetMapping("/{productId}")
    public ProductDto findProductByName(@PathVariable Long productId) {
        return productService.findProductByName(productId);
    }

    @PutMapping("/{productId}")
    public ProductDto updateProductById(@PathVariable Long productId, @RequestBody NewProductDto newProductDto) {
        return productService.updateProductById(productId, newProductDto);
    }

    @DeleteMapping("/{productId}")
    public ProductDto deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }

    @GetMapping
    public Iterable<ProductDto> findAllProducts(){
        return productService.findAllProducts();
    }
}
