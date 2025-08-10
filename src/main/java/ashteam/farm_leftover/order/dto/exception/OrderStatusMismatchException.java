package ashteam.farm_leftover.order.dto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OrderStatusMismatchException extends RuntimeException {
    public OrderStatusMismatchException(String message, String orderStatus) {
        super(message + ": " + orderStatus);
    }
}
