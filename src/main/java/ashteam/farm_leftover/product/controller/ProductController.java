package ashteam.farm_leftover.product.controller;

import ashteam.farm_leftover.product.dto.FarmProductDto;
import ashteam.farm_leftover.product.dto.NewProductDto;
import ashteam.farm_leftover.product.dto.ProductDto;
import ashteam.farm_leftover.product.dto.SearchResultDto;
import ashteam.farm_leftover.product.service.ProductService;
import ashteam.farm_leftover.user.service.UserAccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    final ProductService productService;
    final UserAccountService userAccountService;

    @PostMapping("/add-product")
    public FarmProductDto addProduct(
            Principal principal,
            @RequestParam("newProduct") String newProductJson,
            @RequestParam(value = "file", required = false) MultipartFile file
    ){
        return productService.addProduct(principal.getName(), newProductJson, file);
    }

    @PutMapping("/{productId}")
    public FarmProductDto updateProductById(
            @PathVariable String productId,
            @RequestParam("product") String newProductJson,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Principal principal
    ){
        return productService.updateProductById(productId, newProductJson, principal.getName(), file);
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

    @PostMapping("/{productId}/upload-image")
    public void uploadProductImage(@PathVariable String productId,@RequestParam("file")MultipartFile file, Principal principal){
        productService.uploadProductImage(principal.getName(),productId, file);
    }
}
