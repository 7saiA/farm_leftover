package ashteam.farm_leftover.cart.dto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(Long message) {
        super("Cart Item with id "+message+" not found");
    }
}
