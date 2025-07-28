package ashteam.farm_leftover.product.controller;

import ashteam.farm_leftover.product.dto.FarmProductDto;
import ashteam.farm_leftover.product.dto.NewProductDto;
import ashteam.farm_leftover.product.dto.ProductDto;
import ashteam.farm_leftover.product.dto.SearchResultDto;
import ashteam.farm_leftover.product.service.ProductService;
import ashteam.farm_leftover.user.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    final ProductService productService;
    final UserAccountService userAccountService;

    @PostMapping("/add-product")
    public FarmProductDto addProduct(Principal principal, @RequestBody NewProductDto newProductDto) {
        return productService.addProduct(principal.getName(), newProductDto);
    }

    @PutMapping("/{productId}")
    public FarmProductDto updateProductById(@PathVariable String productId,
                                        @RequestBody NewProductDto newProductDto,
                                        Principal principal) {
        return productService.updateProductById(productId, newProductDto, principal.getName());
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable String productId, Principal principal) {
        productService.deleteProduct(productId, principal.getName());
    }

    @GetMapping("/my-products")
    public Iterable<FarmProductDto> findProductsByFarmId(Principal principal){
        return productService.findProductsByFarmId(principal.getName());
    }

    @GetMapping("/all-products")
    public Iterable<ProductDto> findAllProducts(@RequestParam(defaultValue = "newest") String sort){
        return productService.findAllProducts(sort);
    }

    @GetMapping("/search")
    public SearchResultDto searchAll(@RequestParam String query){
        return new SearchResultDto(
                userAccountService.searchFarms(query),
                productService.searchProducts(query)
        );
    }
}
