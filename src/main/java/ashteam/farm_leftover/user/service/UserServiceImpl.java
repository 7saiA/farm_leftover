package ashteam.farm_leftover.user.service;

import ashteam.farm_leftover.user.dao.UserRepository;
import ashteam.farm_leftover.user.dto.NewUserDto;
import ashteam.farm_leftover.user.dto.UpdatePasswordDto;
import ashteam.farm_leftover.user.dto.UpdateUserDto;
import ashteam.farm_leftover.user.dto.UserDto;
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
    public UserDto createUser(NewUserDto newUserDto) {
        String hashedPassword = BCrypt.hashpw(newUserDto.getPassword(), BCrypt.gensalt(12));
        UserAccount user = new UserAccount(newUserDto.getName(), newUserDto.getEmail(), hashedPassword, newUserDto.getPhone(), newUserDto.getRoleUser());
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto findUserById(Long id) {
        UserAccount user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(Long id, UpdateUserDto updateUserDto) {
        UserAccount user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (updateUserDto.getName() != null) user.setName(updateUserDto.getName());
        if (updateUserDto.getEmail() != null) user.setEmail(updateUserDto.getEmail());
        if (updateUserDto.getPhone() != null) user.setPhone(updateUserDto.getPhone());
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto deleteUser(Long id) {
        UserAccount user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        UserDto dto = modelMapper.map(user, UserDto.class);
        userRepository.deleteById(id);
        return dto;
    }

    @Override
    public UserDto changePassword(Long id, UpdatePasswordDto updatePasswordDto) {
        UserAccount user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (!BCrypt.checkpw(updatePasswordDto.getPassword(), user.getPassword())) {
            throw new UserIncorrectOldPasswordException(updatePasswordDto.getPassword());
        }
        user.setPassword(BCrypt.hashpw(updatePasswordDto.getPassword(), BCrypt.gensalt(12)));
        userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }
}
