package ashteam.farm_leftover.order.service;

import ashteam.farm_leftover.auth.dto.exceptions.UserNotFoundException;
import ashteam.farm_leftover.cart.model.Cart;
import ashteam.farm_leftover.order.dto.OrderResponseDto;
import ashteam.farm_leftover.order.model.Order;
import ashteam.farm_leftover.order.model.OrderItem;
import ashteam.farm_leftover.user.dao.UserAccountRepository;
import ashteam.farm_leftover.user.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    final UserAccountRepository userAccountRepository;
    final ModelMapper modelMapper;

    @Override
    public List<OrderResponseDto> getMyOrders(String login) {
        return List.of();
    }

    @Override
    public List<OrderResponseDto> getFarmOrders(String login) {
        return List.of();
    }

    @Override
    public List<OrderResponseDto> placeOrderFromCart(String login) {
        UserAccount user = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        Cart cart = user.getCart();
        List<OrderItem> items = cart.getItems().stream()
                .map(cartItem -> modelMapper.map(cartItem, OrderItem.class))
                .toList();

        return List.of();
    }

    @Override
    public OrderResponseDto getOrder(String login, String orderId) {
        return null;
    }

    @Override
    public OrderResponseDto cancelOrder(String login, String orderId, String reason) {
        return null;
    }

    @Override
    public OrderResponseDto markAsReadyForPickup(String login, String orderId) {
        return null;
    }

    @Override
    public OrderResponseDto confirmPickupOrComplete(String login, String orderId) {
        return null;
    }
}
