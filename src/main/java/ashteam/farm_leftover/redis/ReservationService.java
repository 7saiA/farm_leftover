package ashteam.farm_leftover.redis;

import ashteam.farm_leftover.cart.dto.exception.EmptyCartException;
import ashteam.farm_leftover.cart.dto.exception.InsufficientQuantityException;
import ashteam.farm_leftover.cart.model.CartItem;
import ashteam.farm_leftover.order.dto.exception.ReservationExpiredException;
import ashteam.farm_leftover.product.dao.ProductRepository;
import ashteam.farm_leftover.product.dto.exceptions.NegativeQuantityException;
import ashteam.farm_leftover.product.dto.exceptions.ProductNotFoundException;
import ashteam.farm_leftover.product.model.Product;
import ashteam.farm_leftover.user.dto.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    final StringRedisTemplate redis;
    final ProductRepository productRepository;

    static final Duration TTL = Duration.ofMinutes(1);
    static final String USER_PREFIX = "reservation:user:";
    static final String PRODUCT_PREFIX = "reservation:product:";  // FIXED: Added colon

    @Async
    public void reserveCart(String userId, List<CartItem> items) {
        validateUser(userId);
        validateItems(items);

        validateStockAvailability(items);

        String userKey = USER_PREFIX + userId;
        Map<String, String> reservations = createReservationMap(items);

        redis.opsForHash().putAll(userKey, reservations);
        redis.expire(userKey, TTL);
        updateProductCounters(items);
    }

    @Transactional
    public void confirmCart(String userId, List<CartItem> items) {
        validateUser(userId);
        validateItems(items);

        String userKey = USER_PREFIX + userId;
        Map<Object, Object> reserved = redis.opsForHash().entries(userKey);

        if (reserved.isEmpty()) {
            throw new ReservationExpiredException();
        }

        updateRedisCounters(items, reserved);

        updateDatabase(items, reserved);

        redis.delete(userKey);
    }

    public int totalReserved(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new ProductNotFoundException("Product ID cannot be empty");
        }

        String value = redis.opsForValue().get(PRODUCT_PREFIX + productId);
        return value != null ? Integer.parseInt(value) : 0;
    }

    private void validateUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new UserNotFoundException("User ID cannot be empty");
        }
    }

    private void validateItems(List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            throw new EmptyCartException();
        }
    }

    private void validateStockAvailability(List<CartItem> items) {
        for (CartItem cartItem : items) {
            String productId = cartItem.getProduct().getProductId();
            int quantity = cartItem.getQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));

            int currentlyReserved = totalReserved(productId);

            if (product.getAvailableQuantity() - currentlyReserved < quantity) {
                throw new InsufficientQuantityException("Not enough stock for product: " + productId);
            }
        }
    }

    private Map<String, String> createReservationMap(List<CartItem> items) {
        return items.stream().collect(Collectors.toMap(
                cartItem -> {
                    String productId = cartItem.getProduct().getProductId();
                    if (productId == null || productId.trim().isEmpty()) {
                        throw new ProductNotFoundException("Product ID cannot be empty");
                    }
                    return productId;
                },
                cartItem -> {
                    int quantity = cartItem.getQuantity();
                    if (quantity <= 0) {
                        throw new NegativeQuantityException("Quantity must be positive");
                    }
                    return String.valueOf(quantity);
                }
        ));
    }

    private void updateProductCounters(List<CartItem> items) {
        items.forEach(cartItem -> {
            String productId = cartItem.getProduct().getProductId();
            redis.opsForValue().increment(PRODUCT_PREFIX + productId, cartItem.getQuantity());
        });
    }

    private void updateRedisCounters(List<CartItem> items, Map<Object, Object> reserved) {
        for (CartItem item : items) {
            String productId = item.getProduct().getProductId();
            String quantityString = (String) reserved.get(productId);

            if (quantityString == null) {
                throw new ReservationExpiredException();
            }

            int reservedQuantity = Integer.parseInt(quantityString);
            redis.opsForValue().decrement(PRODUCT_PREFIX + productId, reservedQuantity);
        }
    }

    private void updateDatabase(List<CartItem> items, Map<Object, Object> reserved) {
        for (CartItem item : items) {
            String productId = item.getProduct().getProductId();
            String qtyStr = (String) reserved.get(productId);
            int reservedQty = Integer.parseInt(qtyStr);

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));

            if (product.getAvailableQuantity() < reservedQty) {
                throw new InsufficientQuantityException(productId);
            }

            product.setAvailableQuantity(product.getAvailableQuantity() - reservedQty);
            productRepository.save(product);
        }
    }
}