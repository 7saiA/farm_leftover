package ashteam.farm_leftover.product.controller;

import ashteam.farm_leftover.product.dto.NewProductDto;
import ashteam.farm_leftover.product.dto.ProductDto;
import ashteam.farm_leftover.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    final ProductService productService;

    @PostMapping("/{farmId}")
    public ProductDto addProduct(@PathVariable Integer farmId, @RequestBody NewProductDto newProductDto) {
        return productService.addProduct(farmId, newProductDto);
    }

    @GetMapping("/{productId}")
    public ProductDto findProductByName(@PathVariable Integer productId) {
        return productService.findProductByName(productId);
    }

    @GetMapping
    public Iterable<ProductDto> findAllProducts(){
        return productService.findAllProducts();
    }

    @PutMapping("/{productId}")
    public ProductDto updateProductById(@PathVariable Integer productId, @RequestBody NewProductDto newProductDto) {
        return productService.updateProductById(productId, newProductDto);
    }

    @DeleteMapping("/{productId}")
    public ProductDto deleteProduct(@PathVariable Integer productId) {
        return productService.deleteProduct(productId);
    }
}
