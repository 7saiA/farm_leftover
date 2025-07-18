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
    public UserProfileDto getCurrentUser(Principal principal){
        return userAccountService.getUser(principal.getName());
    }

    //TODO check if we need it
    @GetMapping("/{login}")
    public UserProfileDto findUserById(@PathVariable String login) {
        return userAccountService.getUser(login);
    }

    @GetMapping("/farms")
    public Iterable<FarmDto> getAllFarms() {
        return userAccountService.getAllFarms();
    }

    @GetMapping("/farms/{farmName}")
    public FarmDto findFarmByFarmName(@PathVariable String farmName) {
        return userAccountService.findFarmByFarmName(farmName);
    }
}
