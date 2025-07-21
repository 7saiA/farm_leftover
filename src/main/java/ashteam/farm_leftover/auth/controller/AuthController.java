package ashteam.farm_leftover.auth.controller;

import ashteam.farm_leftover.auth.dto.LoginPasswordDto;
import ashteam.farm_leftover.auth.dto.UserRegisterDto;
import ashteam.farm_leftover.auth.dto.response.AuthResponse;
import ashteam.farm_leftover.auth.service.AuthService;
import ashteam.farm_leftover.jwt.service.JwtTokenService;
import ashteam.farm_leftover.user.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    final AuthService authService;
    final JwtTokenService jwtTokenService;

    @PostMapping("/register")
    public UserDto register(@RequestBody UserRegisterDto userRegisterDto) {
        return authService.register(userRegisterDto);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginPasswordDto loginPasswordDto,
                                               HttpServletResponse response) {

        AuthResponse authResponse = authService.signIn(loginPasswordDto);
        ResponseCookie refreshTokenCookie = refreshTokenToCookie(authResponse);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authResponse.getAccessToken())
                .body(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
                                       @CookieValue(name = "refreshToken", required = false) String refreshToken,
                                       HttpServletResponse response) {
        String accessToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.substring(7);
        }
        if (accessToken != null || refreshToken != null) {
            authService.logout(accessToken, refreshToken);
        }
        ResponseCookie clearCookie = ResponseCookie.from("refreshToken", "")
                .maxAge(0)
                .path("/auth/refresh-token")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, clearCookie.toString());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/password")
    public void changePassword(Principal principal, @RequestHeader("X-Password") String newPassword) {
        authService.changePassword(principal.getName(), newPassword);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response) {

        AuthResponse authResponse = authService.refreshToken(refreshToken);
        ResponseCookie refreshTokenCookie = refreshTokenToCookie(authResponse);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authResponse.getAccessToken())
                .body(authResponse.withoutRefreshToken());
    }

    private ResponseCookie refreshTokenToCookie(AuthResponse authResponse) {
        return ResponseCookie.from("refreshToken", authResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(jwtTokenService.getRefreshTokenExpirationSeconds())
                .path("/")
                .build();
    }
}
