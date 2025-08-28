package ashteam.farm_leftover.product.dao;

import ashteam.farm_leftover.product.model.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    Collection<Product> findAllByUserAccountLogin(String farmId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.productId = :id")
    Product findProductByIdForUpdate(@Param("id") String id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
    UPDATE product p
    SET reserved_quantity = GREATEST(0, p.reserved_quantity - COALESCE(ci.total_quantity, 0))
    FROM (
        SELECT product_id, SUM(quantity) AS total_quantity
        FROM cart_item
        WHERE product_id IN (:productIds)
          AND reserved_until < :now
          AND reserved_until IS NOT NULL
        GROUP BY product_id
    ) ci
    WHERE p.product_id = ci.product_id
    """,
            nativeQuery = true)
    void releaseExpiredReservationsForProducts(@Param("productIds") List<String> productIds,
                                              @Param("now") LocalDateTime now);

    Collection<Product> findByProductNameContainingIgnoreCase(String query);
}
