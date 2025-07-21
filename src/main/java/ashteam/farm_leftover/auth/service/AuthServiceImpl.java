package ashteam.farm_leftover.auth.service;

import ashteam.farm_leftover.auth.dto.LoginPasswordDto;
import ashteam.farm_leftover.auth.dto.UserRegisterDto;
import ashteam.farm_leftover.auth.dto.exceptions.*;
import ashteam.farm_leftover.auth.dto.response.AuthResponse;
import ashteam.farm_leftover.jwt.service.JwtTokenService;
import ashteam.farm_leftover.user.dao.UserAccountRepository;
import ashteam.farm_leftover.user.dto.UserDto;
import ashteam.farm_leftover.user.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    final UserAccountRepository userAccountRepository;
    final ModelMapper modelMapper;
    final PasswordEncoder passwordEncoder;
    final JwtTokenService jwtTokenService;

    @Transactional
    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        if (userAccountRepository.existsById(userRegisterDto.getLogin())) {
            throw new UserExistsException(userRegisterDto.getLogin());
        }
        if (!userRegisterDto.getLogin().matches("^[a-zA-Z0-9]{3,10}$")) {
            throw new BadLoginNameException();
        }
        if (userRegisterDto.getPassword() == null) {
            throw new BadPasswordException();
        }

        String hashedPassword = passwordEncoder.encode(userRegisterDto.getPassword());
        UserAccount user = new UserAccount(
                userRegisterDto.getLogin(),
                userRegisterDto.getEmail(),
                hashedPassword,
                userRegisterDto.getPhone()
        );

        if (userRegisterDto.getFarmName() != null) {
            user.changeRoleToFarm();
            user.setFarmName(userRegisterDto.getFarmName());
            user.setCity(userRegisterDto.getCity());
            user.setStreet(userRegisterDto.getStreet());
        }

        user = userAccountRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional
    @Override
    public AuthResponse signIn(LoginPasswordDto loginPasswordDto) {
        UserAccount userAccount = userAccountRepository.findById(loginPasswordDto.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(loginPasswordDto.getPassword(), userAccount.getPassword())) {
            throw new BadPasswordException();
        }
        UserDto userDto = modelMapper.map(userAccount, UserDto.class);
        String accessToken = jwtTokenService.generateAccessToken(userDto.getLogin());
        String refreshToken = jwtTokenService.generateRefreshToken(userDto.getLogin());

        jwtTokenService.saveTokens(userDto.getLogin(), accessToken, refreshToken);

        return new AuthResponse(accessToken, refreshToken, userDto);
    }

    @Transactional
    @Override
    public void logout(String accessToken, String refreshToken) {
        if (!jwtTokenService.validateAccessToken(accessToken)
                || !jwtTokenService.validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException();
        }
        String accessLogin = jwtTokenService.extractUsername(accessToken);
        String refreshLogin = jwtTokenService.extractUsername(refreshToken);
        if (!accessLogin.equals(refreshLogin)) {
            throw new IllegalArgumentException();
        }
        jwtTokenService.revokeAllTokens(accessToken, refreshToken);
    }

    @Transactional
    @Override
    public void changePassword(String login, String newPassword) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        String password = passwordEncoder.encode(newPassword);
        userAccount.setPassword(password);
        userAccountRepository.save(userAccount);
    }

    @Transactional
    @Override
    public AuthResponse refreshAccessToken(String refreshToken) {
        if (!jwtTokenService.validateRefreshToken(refreshToken)) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        String login = jwtTokenService.extractUsername(refreshToken);
        UserAccount userAccount = userAccountRepository.findById(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDto userDto = modelMapper.map(userAccount, UserDto.class);
        jwtTokenService.revokeLatestAccessTokensForUser(login);
        String newAccessToken = jwtTokenService.generateAccessToken(login);

        jwtTokenService.saveRefreshAccessToken(login, newAccessToken);

        return new AuthResponse(newAccessToken, refreshToken, userDto);
    }
}
