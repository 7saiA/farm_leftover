package ashteam.farm_leftover.handler;

import ashteam.farm_leftover.auth.dto.exceptions.*;
import ashteam.farm_leftover.cart.dto.exception.*;
import ashteam.farm_leftover.product.dto.exceptions.*;
import io.jsonwebtoken.*;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserExistsException.class)
    public ProblemDetail handleUserExists(UserExistsException e, WebRequest request) {
        return createProblemDetail(
                HttpStatus.CONFLICT,
                "User already exists",
                "USER_ALREADY_EXISTS",
                request
        );
    }

    @ExceptionHandler(BadLoginNameException.class)
    public ProblemDetail handleBadLogin(BadLoginNameException e, WebRequest request) {
        return createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid login format",
                "INVALID_LOGIN_FORMAT",
                request
        );
    }

    @ExceptionHandler(BadPasswordException.class)
    public ProblemDetail handleBadPassword(BadPasswordException e, WebRequest request) {
        return createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid password",
                "EMPTY_PASSWORD",
                request
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ProblemDetail handleUserNotFound(UsernameNotFoundException e, WebRequest request) {
        return createProblemDetail(
                HttpStatus.NOT_FOUND,
                "User not found",
                "USER_NOT_FOUND",
                request
        );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ProblemDetail handleInvalidToken(InvalidTokenException e, WebRequest request) {
        return createProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "Invalid token",
                "INVALID_TOKEN",
                request
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ProblemDetail handleExpiredJwt(ExpiredJwtException e, WebRequest request) {
        return createProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "Token expired",
                "TOKEN_EXPIRED",
                request
        );
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ProblemDetail handleUnsupportedJwt(Exception e, WebRequest request) {
        return createProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "Unsupported token",
                "UNSUPPORTED_TOKEN",
                request
        );
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ProblemDetail handleMalformedJwt(Exception e, WebRequest request) {
        return createProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "Malformed token",
                "MALFORMED_TOKEN",
                request
        );
    }

    @ExceptionHandler(SecurityException.class)
    public ProblemDetail handleSecurityExceptionJwt(Exception e, WebRequest request) {
        return createProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "Security Exception token",
                "SECURITY_EXCEPTION_TOKEN",
                request
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleProductNotFound(ProductNotFoundException e, WebRequest request) {
        return createProblemDetail(
                HttpStatus.NOT_FOUND,
                "Product not found",
                "PRODUCT_NOT_FOUND",
                request
        );
    }

    @ExceptionHandler(InsufficientQuantityException.class)
    public ProblemDetail handleInsufficientQuantity(InsufficientQuantityException e, WebRequest request) {
        return createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Insufficient quantity",
                "INSUFFICIENT_QUANTITY",
                request
        );
    }

    // Cart exceptions
    @ExceptionHandler(CartItemNotFoundException.class)
    public ProblemDetail handleCartItemNotFound(CartItemNotFoundException e, WebRequest request) {
        return createProblemDetail(
                HttpStatus.NOT_FOUND,
                "Cart item not found",
                "CART_ITEM_NOT_FOUND",
                request
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException e, WebRequest request) {
        return createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid request",
                "INVALID_ARGUMENT",
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAllExceptions(Exception e, WebRequest request) {
        return createProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                "INTERNAL_ERROR",
                request
        );
    }

    private ProblemDetail createProblemDetail(HttpStatus status,
                                              String title,
                                              String detail,
                                              WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);
        problem.setProperty("timestamp", Instant.now());

        if (request != null) {
            String path = request.getDescription(false);
            problem.setInstance(URI.create(path));
        }

        return problem;
    }
}