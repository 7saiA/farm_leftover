package ashteam.farm_leftover.paypal.controller;

import ashteam.farm_leftover.cart.dto.exception.EmptyCartException;
import ashteam.farm_leftover.cart.model.Cart;
import ashteam.farm_leftover.order.service.OrderServiceImpl;
import ashteam.farm_leftover.paypal.dto.PayPalApprovalDto;
import ashteam.farm_leftover.paypal.dto.PayPalUrlsDto;
import ashteam.farm_leftover.paypal.service.PayPalService;
import ashteam.farm_leftover.user.dao.UserAccountRepository;
import ashteam.farm_leftover.user.dto.exceptions.UserNotFoundException;
import ashteam.farm_leftover.user.model.UserAccount;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequestMapping("paypal")
@AllArgsConstructor
public class PayPalController {
    final PayPalService payPalService;
    final UserAccountRepository userAccountRepository;
    final OrderServiceImpl orderService;

    @PostMapping("/create")
    public PayPalApprovalDto createPayment(Principal principal, @RequestBody PayPalUrlsDto payPalUrlsDto) throws PayPalRESTException {
        UserAccount userAccount = userAccountRepository.findById(principal.getName())
                .orElseThrow(() -> new UserNotFoundException(principal.getName()));

        Cart cart = userAccount.getCart();
        if (cart == null || cart.getItems().isEmpty()) {
            throw new EmptyCartException();
        }

        orderService.reserveOrder(principal.getName());

        BigDecimal totalPrice = cart.calculateTotal();
        String description = "Farm Leftover Reservation #" + cart.getCartId();

        String approvalUrl = payPalService.createPayment(
          totalPrice,description,payPalUrlsDto.getCancelUrl(),payPalUrlsDto.getSuccessUrl() + "?cartId=" + cart.getCartId()
        );
        return new PayPalApprovalDto(cart.getCartId(),approvalUrl);
    }

    @GetMapping("/success")
    public String paySuccess(Principal principal,@RequestParam String paymentId, @RequestParam String payerId){
        try {
            Payment payment = payPalService.executePayment(paymentId,payerId);
            if("approved".equals(payment.getState())){
                orderService.placeOrder(principal.getName());
                return "Payment successful!";
            }
        }catch (PayPalRESTException e){
            return "Payment failed: " + e.getMessage();
        }
        return "Payment failed";
    }

    @GetMapping("/cancel")
    public String payCancel(Principal principal){
        orderService.cancelReservation(principal.getName());
        return "Payment cancelled";
    }
}
