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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    final UserAccountRepository userAccountRepository;
    final ModelMapper modelMapper;
    final PasswordEncoder passwordEncoder;
    final JwtTokenService jwtTokenService;

    @Transactional
    @Override
    public AuthResponse register(UserRegisterDto userRegisterDto) {
        if (userAccountRepository.existsById(userRegisterDto.getLogin())) {
            throw new BadLoginNameException();
        }
        if (!userRegisterDto.getLogin().matches("^[a-zA-Z0-9]{3,10}$")) {
            throw new BadLoginNameException();
        }
        if (userRegisterDto.getPassword() == null) {
            throw new BadPasswordException();
        }
        UserAccount user = createUser(userRegisterDto);
        user = userAccountRepository.save(user);

        String accessToken = jwtTokenService.generateAccessToken(user.getLogin());
        String refreshToken = jwtTokenService.generateRefreshToken(user.getLogin());

        jwtTokenService.saveTokens(user.getLogin(), accessToken, refreshToken);

        return new AuthResponse(accessToken, refreshToken, user.getRole().name());
    }

    private UserAccount createUser(UserRegisterDto userRegisterDto) {
        String hashedPassword = passwordEncoder.encode(userRegisterDto.getPassword());
        UserAccount user = new UserAccount(
                userRegisterDto.getLogin(),
                userRegisterDto.getEmail(),
                hashedPassword,
                userRegisterDto.getPhone()
        );
        if (userRegisterDto.getUserName() != null) {
            if (!userRegisterDto.getUserName().matches("^[a-zA-Z0-9]{3,10}$")) {
                throw new BadNicknameException();
            }
            Optional<UserAccount> userName = userAccountRepository.findByUserName(userRegisterDto.getUserName());
            if (userName.isPresent()) {
                throw new BadNicknameException();
            }
            user.setUserName(userRegisterDto.getUserName());
        }

        if (userRegisterDto.getFarmName() != null) {
            if (!userRegisterDto.getFarmName().matches("^[a-zA-Z0-9]{3,20}( [a-zA-Z0-9]{3,20})?$")) {
                throw new BadFarmNameException();
            }
            Optional<UserAccount> farmName = userAccountRepository.findByFarmName(userRegisterDto.getFarmName());
            if (farmName.isPresent()) {
                throw new BadFarmNameException();
            }
            user.changeRoleToFarm();
            user.setFarmName(userRegisterDto.getFarmName());
            user.setCity(userRegisterDto.getCity());
            user.setStreet(userRegisterDto.getStreet());
        }

        return user;
    }

    @Transactional
    @Override
    public AuthResponse signIn(LoginPasswordDto loginPasswordDto) {
        UserAccount userAccount = userAccountRepository.findById(loginPasswordDto.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(loginPasswordDto.getPassword(), userAccount.getPassword())) {
            throw new BadPasswordException();
        }
        String accessToken = jwtTokenService.generateAccessToken(userAccount.getLogin());
        String refreshToken = jwtTokenService.generateRefreshToken(userAccount.getLogin());

        jwtTokenService.saveTokens(userAccount.getLogin(), accessToken, refreshToken);

        return new AuthResponse(accessToken, refreshToken, userAccount.getRole().name());
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
        System.out.println("Refresh token received: " + refreshToken);
        System.out.println("Is valid: " + jwtTokenService.validateRefreshToken(refreshToken));
        if (!jwtTokenService.validateRefreshToken(refreshToken)) {
            System.out.println("Refresh token invalid!");
            throw new InvalidTokenException("Invalid refresh token");
        }

        String login = jwtTokenService.extractUsername(refreshToken);

        jwtTokenService.revokeLatestAccessTokensForUser(login);
        String newAccessToken = jwtTokenService.generateAccessToken(login);

        jwtTokenService.saveRefreshAccessToken(login, newAccessToken);

        UserAccount user = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);

        return new AuthResponse(newAccessToken, refreshToken, user.getRole().name());
    }
}
