package ashteam.farm_leftover.cart.service;

import ashteam.farm_leftover.cart.dao.CartRepository;
import ashteam.farm_leftover.cart.dto.AddToCartDto;
import ashteam.farm_leftover.cart.dto.CartItemDto;
import ashteam.farm_leftover.cart.dto.CartResponseDto;
import ashteam.farm_leftover.cart.dto.exception.InsufficientQuantityException;
import ashteam.farm_leftover.cart.model.Cart;
import ashteam.farm_leftover.cart.model.CartItem;
import ashteam.farm_leftover.product.dao.ProductRepository;
import ashteam.farm_leftover.product.dto.exceptions.ProductNotFoundException;
import ashteam.farm_leftover.product.model.Product;
import ashteam.farm_leftover.user.dao.UserAccountRepository;
import ashteam.farm_leftover.user.dto.exceptions.UserNotFoundException;
import ashteam.farm_leftover.user.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    final CartRepository cartRepository;
    final UserAccountRepository userAccountRepository;
    final ProductRepository productRepository;
    final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    @Override
    public CartResponseDto getCartForUser(String login) {
        Cart cart = getOrCreateCart(login);

        List<CartItemDto> itemDtos = cart.getItems().stream()
                .map(item -> modelMapper.map(item, CartItemDto.class))
                .toList();

        return new CartResponseDto(
                cart.getCartId(),
                itemDtos,
                cart.calculateTotal()
        );
    }

    @Transactional
    @Override
    public void addToCart(String login, AddToCartDto dto) {
        if (dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        Cart cart = getOrCreateCart(login);
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(dto.getProductId()));

        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(i -> i.getProduct().equals(product))
                .findFirst();

        int currentQuantity = existingItemOpt.map(CartItem::getQuantity).orElse(0);
        int totalRequestedQuantity = currentQuantity + dto.getQuantity();

        if (totalRequestedQuantity > product.getAvailableQuantity()) {
            throw new InsufficientQuantityException("You are requesting: " + totalRequestedQuantity + "but there is: " + product.getAvailableQuantity());
        }

        if (existingItemOpt.isPresent()) {
            existingItemOpt.get().setQuantity(totalRequestedQuantity);
        } else {
            cart.getItems().add(new CartItem(cart, product, dto.getQuantity()));
        }

        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void clearCart(String login) {
        Cart cart = getOrCreateCart(login);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private Cart getOrCreateCart(String login) {
        UserAccount user = userAccountRepository.findById(login)
                .orElseThrow(() -> new UserNotFoundException(login));
        user.initCart();
        return user.getCart();
    }
}
