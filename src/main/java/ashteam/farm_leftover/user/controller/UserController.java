package ashteam.farm_leftover.user.controller;

import ashteam.farm_leftover.user.dto.NewUserDto;
import ashteam.farm_leftover.user.dto.UpdatePasswordDto;
import ashteam.farm_leftover.user.dto.UpdateUserDto;
import ashteam.farm_leftover.user.dto.UserDto;
import ashteam.farm_leftover.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody NewUserDto newUserDto) {
        return userService.createUser(newUserDto);
    }

    @GetMapping("/{id}")
    public UserDto findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UpdateUserDto updateUserDto) {
        return userService.updateUser(id, updateUserDto);
    }

    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PatchMapping("/{id}")
    public UserDto changePassword(@PathVariable Long id, @RequestBody UpdatePasswordDto updatePasswordDto){
        return userService.changePassword(id,updatePasswordDto);
    }
}
