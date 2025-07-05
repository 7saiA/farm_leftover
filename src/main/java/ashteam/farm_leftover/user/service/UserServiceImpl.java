package ashteam.farm_leftover.user.service;

import ashteam.farm_leftover.security.JwtService;
import ashteam.farm_leftover.user.dao.UserRepository;
import ashteam.farm_leftover.user.dto.*;
import ashteam.farm_leftover.user.dto.exceptions.UnauthorizedException;
import ashteam.farm_leftover.user.dto.exceptions.UserExistsException;
import ashteam.farm_leftover.user.dto.exceptions.UserIncorrectOldPasswordException;
import ashteam.farm_leftover.user.dto.exceptions.UserNotFoundException;
import ashteam.farm_leftover.user.model.Role;
import ashteam.farm_leftover.user.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    final UserRepository userRepository;
    final ModelMapper modelMapper;
    private final JwtService jwtService;

    @Transactional
    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        if (userRepository.existsById(userRegisterDto.getLogin())) {
            throw new UserExistsException(userRegisterDto.getEmail());
        }
        if(userRegisterDto.getLogin().matches(".*[^a-zA-Z0-9].*")){
            throw new UserExistsException(userRegisterDto.getLogin());
        }
        if (userRegisterDto.getFarmName() == null) {
            String hashedPassword = BCrypt.hashpw(userRegisterDto.getPassword(), BCrypt.gensalt(12));
            UserAccount user = new UserAccount(userRegisterDto.getLogin(), userRegisterDto.getEmail(), hashedPassword, userRegisterDto.getPhone());
            user = userRepository.save(user);
            return modelMapper.map(user, UserDto.class);
        } else {
            String hashedPassword = BCrypt.hashpw(userRegisterDto.getPassword(), BCrypt.gensalt(12));
            UserAccount user = new UserAccount(userRegisterDto.getLogin(), userRegisterDto.getEmail(), hashedPassword, userRegisterDto.getPhone(),
                    userRegisterDto.getFarmName(), userRegisterDto.getCity(), userRegisterDto.getStreet());
            user = userRepository.save(user);
            return modelMapper.map(user, UserDto.class);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public UserLoginResponseDto login(UserLoginDto userLoginDto) {
        UserAccount user = userRepository.findById(userLoginDto.getLogin()).orElseThrow(() -> new UnauthorizedException("Invalid Credentials"));
        if(!BCrypt.checkpw(userLoginDto.getPassword(),user.getPassword())){
            throw new UnauthorizedException("Invalid Password");
        }
        String accessToken = jwtService.generateAccessToken(user.getLogin());
        String refreshToken = jwtService.generateRefreshToken(user.getLogin());

        return new UserLoginResponseDto(user.getLogin(),user.getRoles(),accessToken,refreshToken);
    }

    @Transactional(readOnly = true)
    @Override
    public UserLoginResponseDto refreshAccessToken(String refreshToken) {
        String username = jwtService.extractUsernameFromRefreshToken(refreshToken);
        UserAccount user = userRepository.findById(username).orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        UserDetails tempUser = new User(user.getLogin(),user.getPassword(), List.of());
        if(!jwtService.isRefreshTokenValid(refreshToken,tempUser)){
            throw new UnauthorizedException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateAccessToken(user.getLogin());
        return new UserLoginResponseDto(user.getLogin(),user.getRoles(), newAccessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    @Override
    public UserProfileDto getUser(String login) {
        UserAccount user = userRepository.findById(login)
                .orElseThrow(() -> new UserNotFoundException(login));
        return modelMapper.map(user, UserProfileDto.class);
    }

    @Transactional
    @Override
    public UserDto updateUser(String login, UpdateUserDto updateUserDto) {
        UserAccount user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        if (updateUserDto.getLogin() != null) user.setLogin(updateUserDto.getLogin());
        if (updateUserDto.getEmail() != null) user.setEmail(updateUserDto.getEmail());
        if (updateUserDto.getPhone() != null) user.setPhone(updateUserDto.getPhone());
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional
    @Override
    public UserDto deleteUser(String login) {
        UserAccount user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        UserDto dto = modelMapper.map(user, UserDto.class);
        userRepository.deleteById(login);
        return dto;
    }

    @Transactional
    @Override
    public UserDto changePassword(String login, UpdatePasswordDto updatePasswordDto) {
        UserAccount user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        if (!BCrypt.checkpw(updatePasswordDto.getPassword(), user.getPassword())) {
            throw new UserIncorrectOldPasswordException(updatePasswordDto.getPassword());
        }
        user.setPassword(BCrypt.hashpw(updatePasswordDto.getPassword(), BCrypt.gensalt(12)));
        userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<FarmDto> getAllFarms() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRoles().contains(Role.FARM))
                .map(f -> modelMapper.map(f, FarmDto.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public FarmDto findFarmById(String login) {
        UserAccount farm = userRepository.findById(login)
                .orElseThrow(() -> new UserNotFoundException(login));
        return modelMapper.map(farm, FarmDto.class);
    }
}
