package ashteam.farm_leftover.user.service;

import ashteam.farm_leftover.user.dao.UserRepository;
import ashteam.farm_leftover.user.dto.UserRegisterDto;
import ashteam.farm_leftover.user.dto.UpdatePasswordDto;
import ashteam.farm_leftover.user.dto.UpdateUserDto;
import ashteam.farm_leftover.user.dto.UserDto;
import ashteam.farm_leftover.user.dto.exceptions.UserExistsException;
import ashteam.farm_leftover.user.dto.exceptions.UserIncorrectOldPasswordException;
import ashteam.farm_leftover.user.dto.exceptions.UserNotFoundException;
import ashteam.farm_leftover.user.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    final UserRepository userRepository;
    final ModelMapper modelMapper;

    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        if (userRepository.existsById(userRegisterDto.getLogin())) {
            throw new UserExistsException(userRegisterDto.getEmail());
        }
        if(userRegisterDto.getLogin().matches(".*[^a-zA-Z0-9].*")){
            throw new UserExistsException(userRegisterDto.getLogin());
        }
        String hashedPassword = BCrypt.hashpw(userRegisterDto.getPassword(), BCrypt.gensalt(12));
        UserAccount user = new UserAccount(userRegisterDto.getLogin(), userRegisterDto.getEmail(), hashedPassword, userRegisterDto.getPhone());
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getUser(String login) {
        UserAccount user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(String login, UpdateUserDto updateUserDto) {
        UserAccount user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        if (updateUserDto.getLogin() != null) user.setLogin(updateUserDto.getLogin());
        if (updateUserDto.getEmail() != null) user.setEmail(updateUserDto.getEmail());
        if (updateUserDto.getPhone() != null) user.setPhone(updateUserDto.getPhone());
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto deleteUser(String login) {
        UserAccount user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        UserDto dto = modelMapper.map(user, UserDto.class);
        userRepository.deleteById(login);
        return dto;
    }

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
}
