package ashteam.farm_leftover.order.dao;


import ashteam.farm_leftover.order.model.Order;
import ashteam.farm_leftover.user.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,String> {

    List<Order> getOrdersByUser(UserAccount user);

    List<Order> getOrdersByFarm(UserAccount farm);

    @Query("""
    SELECT o FROM Order o
    WHERE o.orderId = :orderId
      AND (o.user = :user OR o.farm = :farm)
""")
    Optional<Order> findByOrderIdAndUserOrFarm(String orderId, UserAccount user, UserAccount farm);
}
