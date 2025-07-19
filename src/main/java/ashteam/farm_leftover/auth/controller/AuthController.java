package ashteam.farm_leftover.auth.controller;

import ashteam.farm_leftover.auth.dto.LoginPasswordDto;
import ashteam.farm_leftover.auth.dto.UserRegisterDto;
import ashteam.farm_leftover.auth.service.AuthService;
import ashteam.farm_leftover.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    final AuthService authService;

    @PostMapping("/register")
    public UserDto register(@RequestBody UserRegisterDto userRegisterDto) {
        return authService.register(userRegisterDto);
    }

    @PostMapping("/sign-in")
    public UserDto signIn(@RequestBody LoginPasswordDto loginPasswordDto) {
        return authService.signIn(loginPasswordDto);
    }

    @PostMapping("/logout/{login}")
    public String logout(Principal principal, @PathVariable String login) {
        return authService.logout(principal.getName(), login);
    }

    @PatchMapping("/password")
    public void changePassword(Principal principal, @RequestHeader("X-Password") String newPassword) {
        authService.changePassword(principal.getName(), newPassword);
    }
}
