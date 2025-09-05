package ashteam.farm_leftover.order.controller;

import ashteam.farm_leftover.order.dto.CancellationReasonDto;
import ashteam.farm_leftover.order.dto.OrderResponseDto;
import ashteam.farm_leftover.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    final OrderService orderService;

    @GetMapping("/customer-orders")
    public List<OrderResponseDto> getMyOrders(Principal principal){
        return orderService.getMyOrders(principal.getName());
    }

    @GetMapping("/farm-orders")
    public List<OrderResponseDto> getFarmOrders(Principal principal) {
        return orderService.getFarmOrders(principal.getName());
    }

    @PostMapping("/place-order")
    public OrderResponseDto placeOrder(Principal principal){return orderService.placeOrder(principal.getName());}

    @PostMapping("/reserve-order")
    public void reserveOrder(Principal principal){
        orderService.reserveOrder(principal.getName());
    }

    @GetMapping("/{orderId}")
    public OrderResponseDto getOrder(Principal principal, @PathVariable String orderId){
        return orderService.getOrder(principal.getName(),orderId);
    }

    @PostMapping("/{orderId}/cancel")
    public OrderResponseDto cancelOrder(Principal principal, @PathVariable String orderId,@RequestBody CancellationReasonDto reason){
        return orderService.cancelOrder(principal.getName(),orderId ,reason);
    }

    @PostMapping("/{orderId}/ready-for-pickup")
    public OrderResponseDto markAsReadyForPickup(
            Principal principal,
            @PathVariable String orderId) {
        return orderService.markAsReadyForPickup(principal.getName(), orderId);
    }

    @PostMapping("/{orderId}/confirm-pickup-or-complete")
    public OrderResponseDto confirmPickupOrComplete(
            Principal principal,
            @PathVariable String orderId) {
        return orderService.confirmPickupOrComplete(principal.getName(), orderId);
    }

    @DeleteMapping("/cancel-reservation")
    public void cancelReservation(Principal principal){
        orderService.cancelReservation(principal.getName());
    }


}
