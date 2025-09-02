package ashteam.farm_leftover.order.service;

import ashteam.farm_leftover.auth.dto.exceptions.UserNotFoundException;
import ashteam.farm_leftover.cart.dao.CartRepository;
import ashteam.farm_leftover.cart.dto.exception.EmptyCartException;
import ashteam.farm_leftover.cart.model.Cart;
import ashteam.farm_leftover.order.dao.OrderRepository;
import ashteam.farm_leftover.order.dto.CancellationReasonDto;
import ashteam.farm_leftover.order.dto.OrderResponseDto;
import ashteam.farm_leftover.order.dto.exception.OrderAccessDeniedException;
import ashteam.farm_leftover.order.dto.exception.OrderNotFoundException;
import ashteam.farm_leftover.order.dto.exception.OrderStatusMismatchException;
import ashteam.farm_leftover.order.model.Order;
import ashteam.farm_leftover.order.model.OrderItem;
import ashteam.farm_leftover.order.model.OrderStatus;
import ashteam.farm_leftover.product.dao.ProductRepository;
import ashteam.farm_leftover.product.dto.exceptions.ProductNotFoundException;
import ashteam.farm_leftover.product.model.Product;
import ashteam.farm_leftover.redis.ReservationService;
import ashteam.farm_leftover.user.dao.UserAccountRepository;
import ashteam.farm_leftover.user.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    final UserAccountRepository userAccountRepository;
    final ModelMapper modelMapper;
    final OrderRepository orderRepository;
    final ProductRepository productRepository;
    final CartRepository cartRepository;
    private final ReservationService reservationService;

    @Transactional(readOnly = true)
    @Override
    public List<OrderResponseDto> getMyOrders(String login) {
        UserAccount user = userAccountRepository.findById(login)
                .orElseThrow(UserNotFoundException::new);
        return orderRepository.getOrdersByUser(user)
                .stream()
                .map(order -> modelMapper.map(order,OrderResponseDto.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderResponseDto> getFarmOrders(String login) {
        UserAccount farm = userAccountRepository.findById(login)
                .orElseThrow(UserNotFoundException::new);
        return orderRepository.getOrdersByFarm(farm)
                .stream()
                .map(order -> modelMapper.map(order,OrderResponseDto.class))
                .toList();
    }

    @Transactional
    @Override
    public void reserveOrder(String login) {
        UserAccount user = userAccountRepository.findById(login)
                .orElseThrow(UserNotFoundException::new);

        Cart cart = user.getCart();
        if(cart == null || cart.getItems().isEmpty()){
            throw new EmptyCartException();
        }

        reservationService.reserveCart(login,cart.getItems());

        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public OrderResponseDto placeOrder(String login) {
        UserAccount user = userAccountRepository.findById(login)
                .orElseThrow(UserNotFoundException::new);

        Cart cart = user.getCart();
        if (cart == null || cart.getItems().isEmpty()) {
            throw new EmptyCartException();
        }

        reservationService.confirmCart(login, cart.getItems());

        Order order = new Order();
        order.setUser(user);
        order.setFarm(cart.getFarm());
        order.setOrderStatus(OrderStatus.CREATED);

        List<OrderItem> orderItems = cart.getItems().stream()
                .sorted(Comparator.comparing(ci -> ci.getProduct().getProductId()))
                .map(cartItem -> {
                    Product product = productRepository.findById(cartItem.getProduct().getProductId())
                            .orElseThrow(() -> new ProductNotFoundException(cartItem.getProduct().getProductId()));

                    OrderItem orderItem = OrderItem.fromCartItem(product, cartItem.getQuantity());
                    orderItem.setOrder(order);
                    return orderItem;
                }).toList();

        order.setItems(orderItems);
        order.calculateTotalPrice();

        cart.getItems().clear();
        cartRepository.save(cart);

        orderRepository.save(order);

        return modelMapper.map(order, OrderResponseDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponseDto getOrder(String login, String orderId) {
        UserAccount user = userAccountRepository.findById(login)
                .orElseThrow(UserNotFoundException::new);

        Order order = orderRepository.findByOrderIdAndUserOrFarm(orderId, user, user)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        return modelMapper.map(order,OrderResponseDto.class);
    }

    @Transactional
    @Override
    public OrderResponseDto cancelOrder(String login, String orderId, CancellationReasonDto reason) {
        UserAccount user = userAccountRepository.findById(login)
                .orElseThrow(UserNotFoundException::new);

        Order order = orderRepository.findByOrderIdAndUserOrFarm(orderId, user, user)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if(order.getOrderStatus() == OrderStatus.CANCELLED_BY_USER || order.getOrderStatus() == OrderStatus.CANCELLED_BY_FARM){
            throw new OrderStatusMismatchException("Order already cancelled!", String.valueOf(order.getOrderStatus()));
        }

        if(order.getUser().equals(user)){
            order.setOrderStatus(OrderStatus.CANCELLED_BY_USER);
            order.setCancelledTime(LocalDateTime.now());
        }else if(order.getFarm().equals(user)){
            order.setOrderStatus(OrderStatus.CANCELLED_BY_FARM);
            order.setCancelledTime(LocalDateTime.now());
        }else {
            throw new OrderAccessDeniedException();
        }
        order.setCancellationReason(reason.getReason());
        orderRepository.save(order);
        return modelMapper.map(order,OrderResponseDto.class);
    }

    @Transactional
    @Override
    public OrderResponseDto markAsReadyForPickup(String login, String orderId) {
        UserAccount user = userAccountRepository.findById(login)
                .orElseThrow(UserNotFoundException::new);

        Order order = orderRepository.findByOrderIdAndUserOrFarm(orderId, user, user)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if(order.getOrderStatus() != OrderStatus.CREATED){
            throw new OrderStatusMismatchException("Can not mark an order as ready for pickup because its status is",String.valueOf(order.getOrderStatus()));
        }

        if (order.getFarm().equals(user)){
            order.setOrderStatus(OrderStatus.READY_FOR_PICKUP);
            order.setReadyForPickupTime(LocalDateTime.now());
            orderRepository.save(order);
        }else {
            throw new OrderAccessDeniedException();
        }
        return modelMapper.map(order,OrderResponseDto.class);
    }

    @Transactional
    @Override
    public OrderResponseDto confirmPickupOrComplete(String login, String orderId) {
        UserAccount user = userAccountRepository.findById(login)
                .orElseThrow(UserNotFoundException::new);

        Order order = orderRepository.findByOrderIdAndUserOrFarm(orderId, user, user)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        boolean currentUserIsFarm = order.getFarm().equals(user);

        OrderStatus status = order.getOrderStatus();

        if (status == OrderStatus.READY_FOR_PICKUP) {
            if (currentUserIsFarm) {
                order.setOrderStatus(OrderStatus.CONFIRMED_BY_FARM);
                order.setFarmConfirmedTime(LocalDateTime.now());
            } else {
                order.setOrderStatus(OrderStatus.CONFIRMED_BY_USER);
                order.setUserConfirmedTime(LocalDateTime.now());
            }
        } else if ((status == OrderStatus.CONFIRMED_BY_USER && currentUserIsFarm) ||
                (status == OrderStatus.CONFIRMED_BY_FARM && !currentUserIsFarm)) {
            order.setOrderStatus(OrderStatus.COMPLETED);
        } else {
            throw new OrderStatusMismatchException(
                    "Order status does not allow confirmation or completion",
                    status.toString()
            );
        }

        orderRepository.save(order);
        return modelMapper.map(order,OrderResponseDto.class);
    }

}
