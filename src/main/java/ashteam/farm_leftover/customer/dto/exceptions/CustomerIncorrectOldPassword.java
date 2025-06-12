package ashteam.farm_leftover.customer.dto.exceptions;

public class CustomerIncorrectOldPassword extends RuntimeException {
    public CustomerIncorrectOldPassword(String message) {
        super("Old password:"+message+" is incorrect");
    }
}
