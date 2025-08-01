package ashteam.farm_leftover.order.dao;


import ashteam.farm_leftover.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,String> {

    List<Order> getOrdersByUserNameOrderByCreatedAtDesc(String userName);

    List<Order> getOrdersByFarmNameOrderByCreatedAtDesc(String userName);
}
