package ashteam.farm_leftover.user.service;

import ashteam.farm_leftover.user.dto.*;

public interface UserService {
    UserProfileDto getUser(String login);
    UserDto updateUser(String login, UpdateUserDto updateUserDto);
    UserDto deleteUser(String login);
    UserDto changePassword(String login, UpdatePasswordDto updatePasswordDto);
    Iterable<FarmDto> getAllFarms();
    FarmDto findFarmById(String login);
}
