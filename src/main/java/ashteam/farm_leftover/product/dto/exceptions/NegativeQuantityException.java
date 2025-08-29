package ashteam.farm_leftover.product.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NegativeQuantityException extends RuntimeException {
    public NegativeQuantityException(String message) {
        super(message);
    }
}
