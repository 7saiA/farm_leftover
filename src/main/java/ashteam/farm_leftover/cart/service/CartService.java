package ashteam.farm_leftover.cart.service;

import ashteam.farm_leftover.cart.dto.AddToCartDto;
import ashteam.farm_leftover.cart.dto.CartResponseDto;

public interface CartService {
    CartResponseDto getCartForUser(String login);

    void addToCart(String login, AddToCartDto dto);

    void clearCart(String login);
}
