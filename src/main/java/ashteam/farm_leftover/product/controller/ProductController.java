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

    @PostMapping("/add-product")
    public ProductDto addProduct(Principal principal, @RequestBody NewProductDto newProductDto) {
        return productService.addProduct(principal.getName(), newProductDto);
    }

    @PutMapping("/{productId}")
    public ProductDto updateProductById(@PathVariable Long productId,
                                        @RequestBody NewProductDto newProductDto,
                                        Principal principal) {
        return productService.updateProductById(productId, newProductDto, principal.getName());
    }

    @DeleteMapping("/{productId}")
    public ProductDto deleteProduct(@PathVariable Long productId, Principal principal) {
        return productService.deleteProduct(productId, principal.getName());
    }

    @GetMapping("/my-products")
    public Iterable<ProductDto> findProductsByFarmId(Principal principal){
        return productService.findProductsByFarmId(principal.getName());
    }

    //TODO speak with Natan to understand why we need it
    @GetMapping("/{productId}")
    public ProductDto findProductByName(@PathVariable Long productId) {
        return productService.findProductByName(productId);
    }

    @GetMapping("/all-products")
    public Iterable<ProductDto> findAllProducts(@RequestParam(defaultValue = "newest") String sort){
        return productService.findAllProducts(sort);
    }
}
