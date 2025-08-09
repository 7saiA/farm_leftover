package ashteam.farm_leftover.cart.dto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CartFarmMismatchException extends RuntimeException {
    public CartFarmMismatchException() {
        super("Can't add products from different farms to the cart");
    }
}
