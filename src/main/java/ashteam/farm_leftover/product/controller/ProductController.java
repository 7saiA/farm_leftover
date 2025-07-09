package ashteam.farm_leftover.product.controller;

import ashteam.farm_leftover.product.dto.NewProductDto;
import ashteam.farm_leftover.product.dto.ProductDto;
import ashteam.farm_leftover.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    final ProductService productService;

    //TODO there are trouble with security

    @PostMapping("/{farmId}")
    public ProductDto addProduct(@PathVariable String farmId, @RequestBody NewProductDto newProductDto) {
        return productService.addProduct(farmId, newProductDto);
    }

    @GetMapping("/myProducts")
    public Iterable<ProductDto> findProductsByFarmId(Principal principal){
        return productService.findProductsByFarm(principal.getName());
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
    public Iterable<ProductDto> findAllProducts(@RequestParam(defaultValue = "newest") String sort){
        return productService.findAllProducts(sort);
    }
}
