package ashteam.farm_leftover.auth.service;

import ashteam.farm_leftover.auth.dto.JwtAuthenticationDto;
import ashteam.farm_leftover.auth.dto.RefreshTokenDto;
import ashteam.farm_leftover.auth.dto.UserCredentialsDto;
import ashteam.farm_leftover.auth.dto.UserRegisterDto;
import ashteam.farm_leftover.auth.dto.exceptions.BadLoginNameException;
import ashteam.farm_leftover.auth.dto.exceptions.BadPasswordException;
import ashteam.farm_leftover.auth.dto.exceptions.UserExistsException;
import ashteam.farm_leftover.security.jwt.JwtService;
import ashteam.farm_leftover.user.dao.UserRepository;
import ashteam.farm_leftover.user.dto.UserDto;
import ashteam.farm_leftover.user.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public JwtAuthenticationDto singIn(UserCredentialsDto userCredentialsDto) {
        UserAccount userAccount = findByCredentials(userCredentialsDto);
        return jwtService.generateAuthToken(userAccount.getLogin());
    }

    @Override
    public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto, Principal principal) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        if (refreshToken == null || !jwtService.validateJwtToken(refreshToken)) {
            throw new BadCredentialsException("Invalid refresh token");
        }
        String tokenUsername = jwtService.getLoginFromToken(refreshToken);
        if (!tokenUsername.equals(principal.getName())) {
            throw new BadCredentialsException("Token does not belong to the user");
        }
        UserAccount userAccount = findByLogin(tokenUsername);
        return jwtService.refreshAccessToken(userAccount.getLogin(), refreshToken);
    }

    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        if (userRepository.existsById(userRegisterDto.getLogin())) {
            throw new UserExistsException(userRegisterDto.getLogin());
        }
        if (userRegisterDto.getLogin().matches(".*[^a-zA-Z0-9]{3,10}$")) {
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

        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    private UserAccount findByCredentials(UserCredentialsDto userCredentialsDto) {
        Optional<UserAccount> optionalUser = userRepository.findById(userCredentialsDto.getLogin());
        if (optionalUser.isPresent()) {
            UserAccount userAccount = optionalUser.get();
            if (passwordEncoder.matches(userCredentialsDto.getPassword(), userAccount.getPassword())) {
                return userAccount;
            }
        }
        throw new BadCredentialsException("Login or Password is not correct");
    }

    private UserAccount findByLogin(String login) {
        return userRepository.findById(login).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
    }
}
