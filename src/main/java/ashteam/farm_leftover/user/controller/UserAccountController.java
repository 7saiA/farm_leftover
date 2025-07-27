package ashteam.farm_leftover.user.controller;

import ashteam.farm_leftover.user.dto.*;
import ashteam.farm_leftover.user.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserAccountController {

    final UserAccountService userAccountService;

    @PutMapping("/update")
    public UserDto updateUser(Principal principal, @RequestBody UpdateUserDto updateUserDto) {
        return userAccountService.updateUser(principal.getName(), updateUserDto);
    }

    @DeleteMapping("/delete")
    public UserDto deleteUser(Principal principal) {
        return userAccountService.deleteUser(principal.getName());
    }

    @GetMapping("/profile")
    public UserDto getCurrentUser(Principal principal){
        return userAccountService.getUser(principal.getName());
    }

    @GetMapping("/farm/{login}")
    public UserDto findFarmById(@PathVariable String login) {
        return userAccountService.findFarmById(login);
    }

    @GetMapping("/farms")
    public Iterable<UserDto> getAllFarms() {
        return userAccountService.getAllFarms();
    }

    @GetMapping("/farms/{farmName}")
    public UserDto findFarmByFarmName(@PathVariable String farmName) {
        return userAccountService.findFarmByFarmName(farmName);
    }
}
