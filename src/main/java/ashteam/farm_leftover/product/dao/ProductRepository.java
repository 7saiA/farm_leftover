package ashteam.farm_leftover.product.dao;

import ashteam.farm_leftover.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Collection<Product> findAllByUserAccountLogin(String farmId);
}
