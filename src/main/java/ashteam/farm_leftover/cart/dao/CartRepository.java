package ashteam.farm_leftover.cart.dao;

import ashteam.farm_leftover.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart,String> {
    @Modifying(clearAutomatically = true,flushAutomatically = true)
    @Query("UPDATE CartItem ci SET ci.reservedUntil = NULL " +
            "WHERE ci.product.productId IN :productIds " +
            "AND ci.reservedUntil < :now " +
            "AND ci.reservedUntil IS NOT NULL")
    void clearExpiredReservationsForCartItems(@Param("productIds") List<String> productIds,
                                            @Param("now") LocalDateTime now);

}
