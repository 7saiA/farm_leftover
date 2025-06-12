package ashteam.farm_leftover.user.service;

import ashteam.farm_leftover.user.dto.NewUserDto;
import ashteam.farm_leftover.user.dto.UpdatePasswordDto;
import ashteam.farm_leftover.user.dto.UpdateUserDto;
import ashteam.farm_leftover.user.dto.UserDto;

public interface UserService {
    UserDto createUser(NewUserDto newUserDto);
    UserDto findUserById(Long id);
    UserDto updateUser(Long id, UpdateUserDto updateUserDto);
    UserDto deleteUser(Long id);
    UserDto changePassword(Long id, UpdatePasswordDto updatePasswordDto);
}
