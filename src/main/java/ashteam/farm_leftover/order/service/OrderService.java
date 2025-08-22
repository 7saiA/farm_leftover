package ashteam.farm_leftover.order.service;

import ashteam.farm_leftover.order.dto.CancellationReasonDto;
import ashteam.farm_leftover.order.dto.OrderResponseDto;
import java.util.List;

public interface OrderService {

    List<OrderResponseDto> getMyOrders(String login);

    List<OrderResponseDto> getFarmOrders(String login);

    void reserveOrder(String login);

    OrderResponseDto getOrder(String login, String orderId);

    OrderResponseDto cancelOrder(String login, String orderId, CancellationReasonDto reason);

    OrderResponseDto markAsReadyForPickup(String login, String orderId);

    OrderResponseDto confirmPickupOrComplete(String login, String orderId);

    OrderResponseDto placeOrder(String login);
}
