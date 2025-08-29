package ashteam.farm_leftover.handler;

import ashteam.farm_leftover.auth.dto.exceptions.*;
import ashteam.farm_leftover.cart.dto.exception.*;
import ashteam.farm_leftover.order.dto.exception.OrderAccessDeniedException;
import ashteam.farm_leftover.order.dto.exception.OrderNotFoundException;
import ashteam.farm_leftover.order.dto.exception.OrderStatusMismatchException;
import ashteam.farm_leftover.order.dto.exception.ReservationExpiredException;
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
    @ExceptionHandler(NegativeQuantityException.class)
    public ResponseEntity<Map<String,Object>> handleNegativeQuantity(NegativeQuantityException e,WebRequest request){
        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "NEGATIVE_PRODUCT_QUANTITY",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(OrderAccessDeniedException.class)
    public ResponseEntity<Map<String,Object>> handleOrderAccessDenied(OrderAccessDeniedException e,WebRequest request){
        return createErrorResponse(
                HttpStatus.FORBIDDEN,
                "ORDER_ACCESS_DENIED",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(ReservationExpiredException.class)
    public ResponseEntity<Map<String,Object>> handleReservationExpired(ReservationExpiredException e, WebRequest request){
        return createErrorResponse(
                HttpStatus.CONFLICT,
                "RESERVATION_EXPIRED",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleOrderNotFound(OrderNotFoundException e, WebRequest request){
        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                "ORDER_NOT_FOUND",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(OrderStatusMismatchException.class)
    public ResponseEntity<Map<String,Object>> handleOrderStatusMismatch(OrderStatusMismatchException e,WebRequest request){
        return createErrorResponse(
                HttpStatus.CONFLICT,
                "ORDER_STATUS_MISMATCH",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<Map<String, Object>> handleEmptyCart(EmptyCartException e, WebRequest request){
        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "EMPTY_CART",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(CartFarmMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleCartFarmMismatch(CartFarmMismatchException e, WebRequest request){
        return createErrorResponse(
            HttpStatus.CONFLICT,
                "CART_FARM_MISMATCH",
                e.getMessage(),
            request
        );
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserExists(UserExistsException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.CONFLICT,
                "USER_ALREADY_EXISTS",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserExists(UserNotFoundException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                "USER_NOT_FOUND",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(BadLoginNameException.class)
    public ResponseEntity<Map<String, Object>> handleBadLogin(BadLoginNameException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.CONFLICT,
                "BAD_LOGIN",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(BadNicknameException.class)
    public ResponseEntity<Map<String, Object>> handleBadLogin(BadNicknameException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.CONFLICT,
                "BAD_NICKNAME",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(BadFarmNameException.class)
    public ResponseEntity<Map<String, Object>> handleBadLogin(BadFarmNameException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.CONFLICT,
                "BAD_FARM_NAME",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(BadPasswordException.class)
    public ResponseEntity<Map<String, Object>> handleBadPassword(BadPasswordException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.CONFLICT,
                "EMPTY_PASSWORD",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UsernameNotFoundException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                "USER_NOT_FOUND",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidToken(InvalidTokenException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "INVALID_TOKEN",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Map<String, Object>> handleExpiredJwt(ExpiredJwtException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "TOKEN_EXPIRED",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<Map<String, Object>> handleUnsupportedJwt(Exception e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "UNSUPPORTED_TOKEN",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<Map<String, Object>> handleMalformedJwt(Exception e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "MALFORMED_TOKEN",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, Object>> handleSecurityExceptionJwt(Exception e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "SECURITY_EXCEPTION_TOKEN",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProductNotFound(ProductNotFoundException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                "PRODUCT_NOT_FOUND",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(InsufficientQuantityException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientQuantity(InsufficientQuantityException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "INSUFFICIENT_QUANTITY",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCartItemNotFound(CartItemNotFoundException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                "CART_ITEM_NOT_FOUND",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "INVALID_ARGUMENT",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception e, WebRequest request) {
        return createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                e.getMessage(),
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