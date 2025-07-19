package ashteam.farm_leftover.auth.service;

import ashteam.farm_leftover.auth.dto.LoginPasswordDto;
import ashteam.farm_leftover.auth.dto.UserRegisterDto;
import ashteam.farm_leftover.auth.dto.exceptions.BadLoginNameException;
import ashteam.farm_leftover.auth.dto.exceptions.BadPasswordException;
import ashteam.farm_leftover.auth.dto.exceptions.UserExistsException;
import ashteam.farm_leftover.auth.dto.exceptions.UserNotFoundException;
import ashteam.farm_leftover.user.dao.UserAccountRepository;
import ashteam.farm_leftover.user.dto.UserDto;
import ashteam.farm_leftover.user.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    final UserAccountRepository userAccountRepository;
    final ModelMapper modelMapper;
    final PasswordEncoder passwordEncoder;

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

    @Override
    public UserDto signIn(LoginPasswordDto loginPasswordDto) {
        UserAccount userAccount = userAccountRepository.findById(loginPasswordDto.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(loginPasswordDto.getPassword(), userAccount.getPassword())) {
            throw new BadPasswordException();
        }
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public String logout(String principalLogin, String login) {
        if (!login.equals(principalLogin)) {
            throw new BadLoginNameException();
        }
        return "Logout is done";
    }

    @Override
    public void changePassword(String login, String newPassword) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        String password = passwordEncoder.encode(newPassword);
        userAccount.setPassword(password);
        userAccountRepository.save(userAccount);
    }
}
