package ashteam.farm_leftover.auth.service;

import ashteam.farm_leftover.auth.dto.LogoutDto;
import ashteam.farm_leftover.security.dto.JwtAuthenticationDto;
import ashteam.farm_leftover.auth.dto.RefreshTokenDto;
import ashteam.farm_leftover.auth.dto.UserCredentialsDto;
import ashteam.farm_leftover.auth.dto.UserRegisterDto;
import ashteam.farm_leftover.auth.dto.UpdatePasswordDto;
import ashteam.farm_leftover.user.dto.UserDto;

import java.security.Principal;

public interface AuthService {
    JwtAuthenticationDto singIn(UserCredentialsDto userCredentialsDto);
    JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto, Principal principal);
    UserDto register(UserRegisterDto userRegisterDto);
    void logout(LogoutDto logoutDto, Principal principal);
    UserDto changePassword(Principal principal, UpdatePasswordDto updatePasswordDto);
}
