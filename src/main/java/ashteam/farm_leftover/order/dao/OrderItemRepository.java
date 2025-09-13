package ashteam.farm_leftover.order.dao;

import ashteam.farm_leftover.order.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
