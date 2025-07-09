package ashteam.farm_leftover.auth.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadLoginNameException extends RuntimeException {
    public BadLoginNameException() {
        super("Bad Login");
    }
}
