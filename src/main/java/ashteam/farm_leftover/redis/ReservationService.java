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
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final StringRedisTemplate redis;
    private final ProductRepository productRepository;

    private static final Duration TTL = Duration.ofMinutes(3);
    private static final String USER_RESERVATION_PREFIX = "reservation:user:";
    private static final String PRODUCT_RESERVATION_PREFIX = "reservation:product:";

    private static final String RESERVE_SCRIPT =
            "local productKey = KEYS[1] " +
                    "local userProductKey = KEYS[2] " +
                    "local quantity = tonumber(ARGV[1]) " +
                    "local available = tonumber(ARGV[2]) " +
                    "local ttl = tonumber(ARGV[3]) " +
                    "local currentReserved = tonumber(redis.call('get', productKey) or 0) " +
                    "if (available - currentReserved) >= quantity then " +
                    "   redis.call('incrby', productKey, quantity) " +
                    "   redis.call('setex', userProductKey, ttl, quantity) " +
                    "   return 1 " +
                    "else " +
                    "   return 0 " +
                    "end";

    @Async
    public void reserveCart(String userId, List<CartItem> items) {
        validateUser(userId);
        validateItems(items);

        DefaultRedisScript<Long> script = new DefaultRedisScript<>(RESERVE_SCRIPT, Long.class);

        for (CartItem item : items) {
            String productId = item.getProduct().getProductId();
            int quantity = item.getQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));

            String productKey = PRODUCT_RESERVATION_PREFIX + productId;
            String userProductKey = USER_RESERVATION_PREFIX + userId + ":" + productId;

            List<String> keys = Arrays.asList(productKey, userProductKey);
            Object[] args = {
                    String.valueOf(quantity),
                    String.valueOf(product.getAvailableQuantity()),
                    String.valueOf(TTL.getSeconds())
            };

            Long result = redis.execute(script, keys, args);

            if (result == 0) {
                cleanupPartialReservations(userId, items);
                throw new InsufficientQuantityException("Not enough stock for product: " + productId);
            }
        }
    }

    @Transactional
    public void confirmCart(String userId, List<CartItem> items) {
        validateUser(userId);
        validateItems(items);

        for (CartItem item : items) {
            String productId = item.getProduct().getProductId();
            String userProductKey = USER_RESERVATION_PREFIX + userId + ":" + productId;
            String productKey = PRODUCT_RESERVATION_PREFIX + productId;

            String reservedQtyStr = redis.opsForValue().get(userProductKey);
            if (reservedQtyStr == null) {
                throw new ReservationExpiredException();
            }

            int reservedQty = Integer.parseInt(reservedQtyStr);

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));

            if (product.getAvailableQuantity() < reservedQty) {
                throw new InsufficientQuantityException("Not enough stock for product: " + productId);
            }

            product.setAvailableQuantity(product.getAvailableQuantity() - reservedQty);
            productRepository.save(product);

            redis.opsForValue().decrement(productKey, reservedQty);
            redis.delete(userProductKey);
        }
    }

    public void cancelReservation(String userId, List<CartItem> items) {
        releaseReservations(userId,items);
    }

    public int totalReserved(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new ProductNotFoundException("Product ID cannot be empty");
        }

        String value = redis.opsForValue().get(PRODUCT_RESERVATION_PREFIX + productId);
        return value != null ? Integer.parseInt(value) : 0;
    }

    private void cleanupPartialReservations(String userId, List<CartItem> items) {
        releaseReservations(userId,items);
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
        for (CartItem item : items) {
            if (item.getQuantity() <= 0) {
                throw new NegativeQuantityException("Quantity must be positive");
            }
        }
    }

    private void releaseReservations(String userId, List<CartItem> items){
        for (CartItem item : items) {
            String productId = item.getProduct().getProductId();
            String userProductKey = USER_RESERVATION_PREFIX + userId + ":" + productId;
            String productKey = PRODUCT_RESERVATION_PREFIX + productId;

            String reservedQtyStr = redis.opsForValue().get(userProductKey);
            if (reservedQtyStr != null) {
                int reservedQty = Integer.parseInt(reservedQtyStr);
                redis.opsForValue().decrement(productKey, reservedQty);
                redis.delete(userProductKey);
            }
        }
    }
}