package ashteam.farm_leftover.user.service;

import ashteam.farm_leftover.user.dao.UserAccountRepository;
import ashteam.farm_leftover.user.dto.*;
import ashteam.farm_leftover.user.dto.exceptions.UserNotFoundException;
import ashteam.farm_leftover.user.model.Role;
import ashteam.farm_leftover.user.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    final UserAccountRepository userAccountRepository;
    final ModelMapper modelMapper;
    final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserDto updateUser(String login, UpdateUserDto updateUserDto) {
        UserAccount user = userAccountRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        if (updateUserDto.getEmail() != null) user.setEmail(updateUserDto.getEmail());
        if (updateUserDto.getPhone() != null) user.setPhone(updateUserDto.getPhone());
        user = userAccountRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional
    @Override
    public UserDto deleteUser(String login) {
        UserAccount user = userAccountRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        UserDto dto = modelMapper.map(user, UserDto.class);
        userAccountRepository.deleteById(login);
        return dto;
    }

    @Transactional(readOnly = true)
    @Override
    public UserProfileDto getUser(String login) {
        UserAccount user = userAccountRepository.findById(login)
                .orElseThrow(() -> new UserNotFoundException(login));
        return modelMapper.map(user, UserProfileDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<FarmDto> getAllFarms() {
        return userAccountRepository.findAll().stream()
                .filter(u -> u.getRole().equals(Role.FARM))
                .map(f -> modelMapper.map(f, FarmDto.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public FarmDto findFarmByFarmName(String farmName) {
        UserAccount farm = userAccountRepository.findByFarmNameIgnoreCase(farmName);
        return modelMapper.map(farm, FarmDto.class);
    }
}
