package ashteam.farm_leftover.farm.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class FarmNotFoundException extends RuntimeException {
    public FarmNotFoundException(Integer message) {
        super("Farm with id "+message+" not found");
    }
}
