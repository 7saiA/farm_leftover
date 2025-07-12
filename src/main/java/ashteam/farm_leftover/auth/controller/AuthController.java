package ashteam.farm_leftover.auth.controller;

import ashteam.farm_leftover.auth.dto.LogoutDto;
import ashteam.farm_leftover.security.dto.JwtAuthenticationDto;
import ashteam.farm_leftover.auth.dto.RefreshTokenDto;
import ashteam.farm_leftover.auth.dto.UserCredentialsDto;
import ashteam.farm_leftover.auth.dto.UserRegisterDto;
import ashteam.farm_leftover.auth.service.AuthService;
import ashteam.farm_leftover.auth.dto.UpdatePasswordDto;
import ashteam.farm_leftover.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sing-in")
    public JwtAuthenticationDto singIn(@RequestBody UserCredentialsDto userCredentialsDto) {
        return authService.singIn(userCredentialsDto);
    }

    @PostMapping("/refresh")
    public JwtAuthenticationDto refresh(@RequestBody RefreshTokenDto refreshTokenDto, Principal principal) {
        return authService.refreshToken(refreshTokenDto, principal);
    }

    @PostMapping("/register")
    public UserDto register(@RequestBody UserRegisterDto userRegisterDto) {
        return authService.register(userRegisterDto);
    }

    @PostMapping("/logout")
    public void logout(@RequestBody LogoutDto logoutDto, Principal principal) {
        authService.logout(logoutDto, principal);
    }

    @PatchMapping("/password")
    public UserDto changePassword(Principal principal, @RequestBody UpdatePasswordDto updatePasswordDto){
        return authService.changePassword(principal, updatePasswordDto);
    }
}
