package ashteam.farm_leftover.user.service;

import ashteam.farm_leftover.user.dto.*;

import java.security.Principal;

public interface UserService {
    UserDto updateUser(String login, UpdateUserDto updateUserDto, Principal principal);
    UserDto deleteUser(String login, Principal principal);
    UserDto changePassword(String login, UpdatePasswordDto updatePasswordDto);
    UserProfileDto getUser(String login);
    Iterable<FarmDto> getAllFarms();
    FarmDto findFarmById(String login);
}
