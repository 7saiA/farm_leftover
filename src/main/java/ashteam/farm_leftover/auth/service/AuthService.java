package ashteam.farm_leftover.auth.service;

import ashteam.farm_leftover.auth.dto.LoginPasswordDto;
import ashteam.farm_leftover.auth.dto.UserRegisterDto;
import ashteam.farm_leftover.auth.dto.response.AuthResponse;
import ashteam.farm_leftover.user.dto.UserDto;

public interface AuthService {
    UserDto register(UserRegisterDto userRegisterDto);

    AuthResponse signIn(LoginPasswordDto loginPasswordDto);

    void logout(String accessToken, String refreshToken);

    void changePassword(String login, String newPassword);

    AuthResponse refreshAccessToken(String refreshToken);
}
