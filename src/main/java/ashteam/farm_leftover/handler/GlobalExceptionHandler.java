package ashteam.farm_leftover.handler;

import ashteam.farm_leftover.auth.dto.exceptions.*;
import ashteam.farm_leftover.cart.dto.exception.*;
import ashteam.farm_leftover.product.dto.exceptions.*;
import io.jsonwebtoken.*;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<Map<String, Object>> handleEmptyCart(EmptyCartException e, WebRequest request){
        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Your cart is empty",
                "EMPTY_CART",
                request
        );
    };

    @ExceptionHandler(CartFarmMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleCartFarmMismatch(CartFarmMismatchException e, WebRequest request){
        return createErrorResponse(
            HttpStatus.CONFLICT,
            "Can't add products from different farms to the cart",
            "CART_FARM_MISMATCH",
            request
        );
    };

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserExists(UserExistsException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.CONFLICT,
                "User already exists",
                "USER_ALREADY_EXISTS",
                request
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserExists(UserNotFoundException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                "User not found",
                "USER_NOT_FOUND",
                request
        );
    }

    @ExceptionHandler(BadLoginNameException.class)
    public ResponseEntity<Map<String, Object>> handleBadLogin(BadLoginNameException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid login format",
                "INVALID_LOGIN_FORMAT",
                request
        );
    }

    @ExceptionHandler(BadPasswordException.class)
    public ResponseEntity<Map<String, Object>> handleBadPassword(BadPasswordException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid password",
                "EMPTY_PASSWORD",
                request
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UsernameNotFoundException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                "User not found",
                "USER_NOT_FOUND",
                request
        );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidToken(InvalidTokenException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Invalid token",
                "INVALID_TOKEN",
                request
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Map<String, Object>> handleExpiredJwt(ExpiredJwtException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Token expired",
                "TOKEN_EXPIRED",
                request
        );
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<Map<String, Object>> handleUnsupportedJwt(Exception e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Unsupported token",
                "UNSUPPORTED_TOKEN",
                request
        );
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<Map<String, Object>> handleMalformedJwt(Exception e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Malformed token",
                "MALFORMED_TOKEN",
                request
        );
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, Object>> handleSecurityExceptionJwt(Exception e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Security Exception token",
                "SECURITY_EXCEPTION_TOKEN",
                request
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProductNotFound(ProductNotFoundException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                "Product not found",
                "PRODUCT_NOT_FOUND",
                request
        );
    }

    @ExceptionHandler(InsufficientQuantityException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientQuantity(InsufficientQuantityException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Insufficient quantity",
                "INSUFFICIENT_QUANTITY",
                request
        );
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCartItemNotFound(CartItemNotFoundException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                "Cart item not found",
                "CART_ITEM_NOT_FOUND",
                request
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid request",
                "INVALID_ARGUMENT",
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                "INTERNAL_ERROR",
                request
        );
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(
            HttpStatus status,
            String code,
            String message,
            WebRequest request
    ) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("code", code);
        body.put("message", message);
        body.put("timestamp", Instant.now());
        body.put("path", request.getDescription(false));

        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }
}