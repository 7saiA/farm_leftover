package ashteam.farm_leftover.user.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class FarmNotFoundException extends RuntimeException {
    public FarmNotFoundException(String message) {
        super(message+" not found");
    }
}
