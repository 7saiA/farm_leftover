package ashteam.farm_leftover.cart.dao;

import ashteam.farm_leftover.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,String> {
    Cart findCartByUserAccount_Login(String login);

}
