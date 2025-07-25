package ashteam.farm_leftover.cart.controller;

import ashteam.farm_leftover.cart.dto.AddToCartDto;
import ashteam.farm_leftover.cart.dto.CartResponseDto;
import ashteam.farm_leftover.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    final CartService cartService;

    @GetMapping
    public CartResponseDto getCart(Principal principal){
        return cartService.getCartForUser(principal.getName());
    }

    @PostMapping("/add")
    public void addToCart(@RequestBody AddToCartDto dto, Principal principal) {
        cartService.addToCart(principal.getName(), dto);
    }

    @DeleteMapping("/clear")
    public void clearCart(Principal principal) {
        cartService.clearCart(principal.getName());
    }

    @DeleteMapping("/{cartItemId}")
    public void deleteCartItem(Principal principal, @PathVariable Long cartItemId){
        cartService.deleteCartItem(principal.getName(),cartItemId);
    }
}
