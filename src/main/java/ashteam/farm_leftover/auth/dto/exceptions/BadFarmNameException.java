package ashteam.farm_leftover.auth.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BadFarmNameException extends RuntimeException {
    public BadFarmNameException() {
        super("Bad Farm Name");
    }
}
