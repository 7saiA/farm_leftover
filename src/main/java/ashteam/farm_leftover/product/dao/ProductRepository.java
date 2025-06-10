package ashteam.farm_leftover.product.dao;

import ashteam.farm_leftover.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
