package ashteam.farm_leftover.user.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserIncorrectOldPasswordException extends RuntimeException {
    public UserIncorrectOldPasswordException(String message) {
        super("Old password:"+message+" is incorrect");
    }
}
