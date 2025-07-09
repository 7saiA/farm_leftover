package ashteam.farm_leftover.user.service;

import ashteam.farm_leftover.user.dao.UserRepository;
import ashteam.farm_leftover.user.dto.*;
import ashteam.farm_leftover.user.dto.exceptions.UserIncorrectOldPasswordException;
import ashteam.farm_leftover.user.dto.exceptions.UserNotFoundException;
import ashteam.farm_leftover.user.model.Role;
import ashteam.farm_leftover.user.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    final UserRepository userRepository;
    final ModelMapper modelMapper;

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
                .filter(u -> u.getRole().equals(Role.FARM))
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
