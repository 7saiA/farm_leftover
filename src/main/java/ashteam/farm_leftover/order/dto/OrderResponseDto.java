package ashteam.farm_leftover.order.dto;

import ashteam.farm_leftover.order.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponseDto {
    String orderId;
    OrderStatus orderStatus;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    LocalDateTime readyForPickupTime;
    LocalDateTime userConfirmedTime;
    LocalDateTime farmConfirmedTime;
    LocalDateTime cancelledTime;

    String username;
    String userPhone;
    String farmName;
    String city;
    String street;

    List<OrderItemDto> items;
    BigDecimal totalPrice;
    String cancellationReason;
}

