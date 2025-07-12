package ashteam.farm_leftover.user.service;

import ashteam.farm_leftover.user.dao.UserRepository;
import ashteam.farm_leftover.user.dto.*;
import ashteam.farm_leftover.user.dto.exceptions.UserNotFoundException;
import ashteam.farm_leftover.user.model.Role;
import ashteam.farm_leftover.user.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    final UserRepository userRepository;
    final ModelMapper modelMapper;
    final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserDto updateUser(String login, UpdateUserDto updateUserDto, Principal principal) {
        if (!login.equals(principal.getName())) {
            throw new AccessDeniedException("You can only update your own account");
        }
        UserAccount user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        if (updateUserDto.getEmail() != null) user.setEmail(updateUserDto.getEmail());
        if (updateUserDto.getPhone() != null) user.setPhone(updateUserDto.getPhone());
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional
    @Override
    public UserDto deleteUser(String login, Principal principal) {
        if (!login.equals(principal.getName())) {
            throw new AccessDeniedException("You can only delete your own account");
        }
        UserAccount user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        UserDto dto = modelMapper.map(user, UserDto.class);
        userRepository.deleteById(login);
        return dto;
    }

    //TODO check all of methods below, to security

    @Transactional(readOnly = true)
    @Override
    public UserProfileDto getUser(String login) {
        UserAccount user = userRepository.findById(login)
                .orElseThrow(() -> new UserNotFoundException(login));
        return modelMapper.map(user, UserProfileDto.class);
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
