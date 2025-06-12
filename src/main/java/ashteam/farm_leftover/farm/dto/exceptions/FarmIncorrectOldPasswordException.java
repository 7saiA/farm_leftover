package ashteam.farm_leftover.farm.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FarmIncorrectOldPasswordException extends RuntimeException {
    public FarmIncorrectOldPasswordException(String message) {
        super("Old password:"+message+" is incorrect");
    }
}
