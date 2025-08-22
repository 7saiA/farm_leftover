package ashteam.farm_leftover.order.dto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ReservationExpiredException extends RuntimeException {
    public ReservationExpiredException() {
        super("Reservation expired. Retry from the start.");
    }
}
