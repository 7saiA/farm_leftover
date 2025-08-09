package ashteam.farm_leftover.order.service;

import ashteam.farm_leftover.auth.dto.exceptions.UserNotFoundException;
import ashteam.farm_leftover.cart.dao.CartRepository;
import ashteam.farm_leftover.cart.dto.exception.EmptyCartException;
import ashteam.farm_leftover.cart.model.Cart;
import ashteam.farm_leftover.order.dao.OrderRepository;
import ashteam.farm_leftover.order.dto.OrderResponseDto;
import ashteam.farm_leftover.order.dto.exception.OrderNotFoundException;
import ashteam.farm_leftover.order.model.Order;
import ashteam.farm_leftover.order.model.OrderItem;
import ashteam.farm_leftover.order.model.OrderStatus;
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
    final OrderRepository orderRepository;
    final CartRepository cartRepository;

    @Override
    public List<OrderResponseDto> getMyOrders(String login) {
        UserAccount user = userAccountRepository.findById(login)
                .orElseThrow(UserNotFoundException::new);
        return orderRepository.getOrdersByUser(user)
                .stream()
                .map(order -> modelMapper.map(order,OrderResponseDto.class))
                .toList();
    }

    @Override
    public List<OrderResponseDto> getFarmOrders(String login) {
        UserAccount farm = userAccountRepository.findById(login)
                .orElseThrow(UserNotFoundException::new);
        return orderRepository.getOrdersByFarm(farm)
                .stream()
                .map(order -> modelMapper.map(order,OrderResponseDto.class))
                .toList();
    }

    @Override
    public OrderResponseDto placeOrderFromCart(String login) {
        UserAccount user = userAccountRepository.findById(login)
                .orElseThrow(UserNotFoundException::new);

        Cart cart = user.getCart();
        if (cart == null || cart.getItems().isEmpty()) {
            throw new EmptyCartException();
        }

        Order order = new Order();

        List<OrderItem> orderItems = cart.getItems()
                .stream()
                .map(cartItem -> OrderItem.fromCartItem(
                        cartItem.getProduct(),
                        cartItem.getQuantity()
                ))
                .peek(cartItem -> cartItem.setOrder(order))
                .toList();

        order.setUser(user);
        order.setFarm(cart.getFarm());
        order.setOrderStatus(OrderStatus.CREATED);
        order.setItems(orderItems);
        order.calculateTotalPrice();

        Order savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        cart.setFarm(null);
        cartRepository.save(cart);

        return modelMapper.map(savedOrder, OrderResponseDto.class);
    }



    @Override
    public OrderResponseDto getOrder(String login, String orderId) {
        UserAccount user = userAccountRepository.findById(login)
                .orElseThrow(UserNotFoundException::new);

        Order order = orderRepository.findByOrderIdAndUserOrFarm(orderId, user, user)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        return modelMapper.map(order,OrderResponseDto.class);
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
