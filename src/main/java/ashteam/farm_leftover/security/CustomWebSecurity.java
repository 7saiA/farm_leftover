package ashteam.farm_leftover.security;

import ashteam.farm_leftover.product.dao.ProductRepository;
import ashteam.farm_leftover.product.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("webSecurity")
@RequiredArgsConstructor
public class CustomWebSecurity {

    final ProductRepository productRepository;

    public boolean checkProductUser(Long productId, String username) {
        Product product = productRepository.findById(productId).orElse(null);
        return product != null && product.getUserAccount().getLogin().equalsIgnoreCase(username);
    }
}
