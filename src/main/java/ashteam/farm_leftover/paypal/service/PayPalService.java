package ashteam.farm_leftover.paypal.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@AllArgsConstructor
public class PayPalService {
    final APIContext apiContext;


    public String createPayment(BigDecimal amount, String description, String cancelUrl, String successUrl) throws PayPalRESTException {
        Amount payAmount = new Amount();
        payAmount.setCurrency("USD");
        payAmount.setTotal(amount.setScale(2, RoundingMode.HALF_UP).toPlainString());

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(payAmount);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(List.of(transaction));

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        Payment createdPayment = payment.create(apiContext);

        return createdPayment.getLinks().stream()
                .filter(links -> "approval_url".equals(links.getRel()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No approval url"))
                .getHref();
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution execution = new PaymentExecution();
        execution.setPayerId(payerId);

        return payment.execute(apiContext,execution);
    }
}
