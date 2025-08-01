package ashteam.farm_leftover.user.controller;

import ashteam.farm_leftover.user.dto.*;
import ashteam.farm_leftover.user.service.UserAccountService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
    public void deleteUser(Principal principal, HttpServletResponse response) {
        userAccountService.deleteUser(principal.getName());
    }

    @GetMapping("/profile")
    public UserDto getCurrentUser(Principal principal){
        return userAccountService.getUser(principal.getName());
    }

    @GetMapping("/farm/{farmName}")
    public FarmDto findFarmByFarmName(@PathVariable String farmName) {
        return userAccountService.findFarmByFarmName(farmName);
    }

    @GetMapping("/farms")
    public Iterable<AllFarmsDto> getAllFarms() {
        return userAccountService.getAllFarms();
    }
}
