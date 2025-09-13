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
    private static final String PRODUCT_STOCK_PREFIX = "stock:product:";

    private static final String RESERVE_LUA =
            "local n = #ARGV - 1 " +
                    "local ttl = tonumber(ARGV[#ARGV]) " +
                    "local diffs = {} " +
                    "for i=1,n do " +
                    "  local stockKey = KEYS[i*2-1] " +
                    "  local userKey = KEYS[i*2] " +
                    "  local desired = tonumber(ARGV[i]) " +
                    "  local reserved = tonumber(redis.call('get', userKey) or '0') " +
                    "  local diff = desired - reserved " +
                    "  diffs[i] = diff " +
                    "  local stock = tonumber(redis.call('get', stockKey) or '-1') " +
                    "  if stock == -1 then return -2 end " +
                    "  if diff > 0 and stock < diff then return i end " +
                    "end " +
                    "for i=1,n do " +
                    "  local stockKey = KEYS[i*2-1] " +
                    "  local userKey = KEYS[i*2] " +
                    "  local diff = diffs[i] " +
                    "  if diff > 0 then redis.call('decrby', stockKey, diff) end " +
                    "  if diff < 0 then redis.call('incrby', stockKey, -diff) end " +
                    "  redis.call('set', userKey, ARGV[i]) " +
                    "  redis.call('expire', userKey, ttl) " +
                    "end " +
                    "return 0";

    public void reserveCart(String userId, List<CartItem> items) {
        validateUser(userId);
        validateItems(items);

        DefaultRedisScript<Long> script = new DefaultRedisScript<>(RESERVE_LUA, Long.class);

        List<String> keys = new ArrayList<>();
        List<String> args = new ArrayList<>();

        for (CartItem item : items) {
            String productId = item.getProduct().getProductId();
            int desiredQty = item.getQuantity();

            String stockKey = PRODUCT_STOCK_PREFIX + productId;
            if (!Boolean.TRUE.equals(redis.hasKey(stockKey))) {
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new ProductNotFoundException(productId));
                redis.opsForValue().set(stockKey, String.valueOf(product.getAvailableQuantity()));
            }

            String userKey = USER_RESERVATION_PREFIX + userId + ":" + productId;
            keys.add(stockKey);
            keys.add(userKey);
            args.add(String.valueOf(desiredQty));
        }

        args.add(String.valueOf(TTL.getSeconds()));

        long result = redis.execute(script, keys, args.toArray());

        if (result == -2) throw new ProductNotFoundException("Stock key not found");
        if (result > 0) {
            CartItem failedItem = items.get((int) result - 1);
            throw new InsufficientQuantityException("Not enough stock for product: " + failedItem.getProduct().getProductId());
        }
    }

    @Transactional
    public void confirmCart(String userId, List<CartItem> items) {
        validateUser(userId);
        validateItems(items);

        for (CartItem item : items) {
            String productId = item.getProduct().getProductId();
            String userKey = USER_RESERVATION_PREFIX + userId + ":" + productId;

            String reservedStr = redis.opsForValue().get(userKey);
            if (reservedStr == null) {
                throw new ReservationExpiredException();
            }
            int reservedQty = Integer.parseInt(reservedStr);

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));

            if (product.getAvailableQuantity() < reservedQty) {
                throw new InsufficientQuantityException("Not enough stock in DB for product: " + productId);
            }

            product.setAvailableQuantity(product.getAvailableQuantity() - reservedQty);
            productRepository.save(product);

            redis.delete(userKey);
        }
    }

    public void cancelReservation(String userId, List<CartItem> items) {
        for (CartItem item : items) {
            String productId = item.getProduct().getProductId();
            String userKey = USER_RESERVATION_PREFIX + userId + ":" + productId;

            String reservedStr = redis.opsForValue().get(userKey);
            if (reservedStr != null) {
                int reservedQty = Integer.parseInt(reservedStr);
                redis.opsForValue().increment(PRODUCT_STOCK_PREFIX + productId, reservedQty);
                redis.delete(userKey);
            }
        }
    }

    public int getAvailableStock(String productId) {
        String stockKey = PRODUCT_STOCK_PREFIX + productId;
        String stockStr = redis.opsForValue().get(stockKey);
        if (stockStr != null) return Integer.parseInt(stockStr);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return product.getAvailableQuantity();
    }

    private void validateUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new UserNotFoundException("User ID cannot be empty");
        }
    }

    private void validateItems(List<CartItem> items) {
        if (items == null || items.isEmpty()) throw new EmptyCartException();
        for (CartItem item : items) {
            if (item.getQuantity() <= 0) {
                throw new NegativeQuantityException("Quantity must be positive");
            }
        }
    }
}