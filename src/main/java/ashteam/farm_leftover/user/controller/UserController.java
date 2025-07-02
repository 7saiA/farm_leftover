package ashteam.farm_leftover.user.controller;

import ashteam.farm_leftover.user.dto.*;
import ashteam.farm_leftover.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    final UserService userService;

    @PostMapping("/register")
    public UserDto register(@RequestBody UserRegisterDto userRegisterDto) {
        return userService.register(userRegisterDto);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }

    @PostMapping("/refresh")
    public UserLoginResponseDto refreshToken(@RequestBody UserRefreshTokenRequestDto refreshTokenRequestDto){
        return userService.refreshAccessToken(refreshTokenRequestDto.getRefreshToken());
    }

    @GetMapping("/profile")
    public UserProfileDto getCurrentUser(Principal principal){
        return userService.getUser(principal.getName());
    }

    @GetMapping("/{login}")
    public UserProfileDto findUserById(@PathVariable String login) {
        return userService.getUser(login);
    }

    @PutMapping("/{login}")
    public UserDto updateUser(@PathVariable String login, @RequestBody UpdateUserDto updateUserDto) {
        return userService.updateUser(login, updateUserDto);
    }

    @DeleteMapping("/{login}")
    public UserDto deleteUser(@PathVariable String login) {
        return userService.deleteUser(login);
    }

    @PatchMapping("/password")
    public UserDto changePassword(Principal principal, @RequestBody UpdatePasswordDto updatePasswordDto){
        return userService.changePassword(principal.getName(),updatePasswordDto);
    }


}
