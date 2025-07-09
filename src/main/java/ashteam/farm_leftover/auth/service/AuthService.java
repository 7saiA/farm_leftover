package ashteam.farm_leftover.auth.service;

import ashteam.farm_leftover.auth.dto.JwtAuthenticationDto;
import ashteam.farm_leftover.auth.dto.RefreshTokenDto;
import ashteam.farm_leftover.auth.dto.UserCredentialsDto;
import ashteam.farm_leftover.auth.dto.UserRegisterDto;
import ashteam.farm_leftover.user.dto.UserDto;

import javax.naming.AuthenticationException;
import java.security.Principal;

public interface AuthService {
    JwtAuthenticationDto singIn(UserCredentialsDto userCredentialsDto);
    JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto, Principal principal);

    UserDto register(UserRegisterDto userRegisterDto);
}
