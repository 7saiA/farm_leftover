package ashteam.farm_leftover.auth.service;

import ashteam.farm_leftover.auth.dto.LoginPasswordDto;
import ashteam.farm_leftover.auth.dto.UserRegisterDto;
import ashteam.farm_leftover.user.dto.UserDto;

public interface AuthService {
    UserDto register(UserRegisterDto userRegisterDto);

    UserDto signIn(LoginPasswordDto loginPasswordDto);

    String logout(String principalLogin, String login);

    void changePassword(String login, String newPassword);
}
