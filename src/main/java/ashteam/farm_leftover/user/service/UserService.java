package ashteam.farm_leftover.user.service;

import ashteam.farm_leftover.user.dto.UserRegisterDto;
import ashteam.farm_leftover.user.dto.UpdatePasswordDto;
import ashteam.farm_leftover.user.dto.UpdateUserDto;
import ashteam.farm_leftover.user.dto.UserDto;

public interface UserService {
    UserDto register(UserRegisterDto userRegisterDto);
    UserDto getUser(String login);
    UserDto updateUser(String login, UpdateUserDto updateUserDto);
    UserDto deleteUser(String login);
    UserDto changePassword(String login, UpdatePasswordDto updatePasswordDto);
}
