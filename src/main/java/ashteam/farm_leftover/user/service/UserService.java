package ashteam.farm_leftover.user.service;

import ashteam.farm_leftover.user.dto.*;

public interface UserService {
    UserDto register(UserRegisterDto userRegisterDto);
    UserDto getUser(String login);
    UserDto updateUser(String login, UpdateUserDto updateUserDto);
    UserDto deleteUser(String login);
    UserDto changePassword(String login, UpdatePasswordDto updatePasswordDto);
    UserLoginResponseDto login(UserLoginDto userLoginDto);
    UserLoginResponseDto refreshAccessToken(String refreshToken);
}
