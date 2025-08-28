package ashteam.farm_leftover.geocoding.dto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GeocodeException extends RuntimeException {
    public GeocodeException(String message) {
        super(message);
    }
}
