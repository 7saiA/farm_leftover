package ashteam.farm_leftover.order.dto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class OrderAccessDeniedException extends RuntimeException {
    public OrderAccessDeniedException() {
        super("You are not a part of this order");
    }
}
