package ashteam.farm_leftover.user.service;

import ashteam.farm_leftover.user.dto.*;

public interface UserAccountService {
    UserDto updateUser(String login, UpdateUserDto updateUserDto);
    UserDto deleteUser(String login);
    UserDto getUser(String login);
    Iterable<UserDto> getAllFarms();
    UserDto findFarmByFarmName(String farmName);
    Iterable<UserDto> searchFarms(String query);
    UserDto findFarmById(String login);
}
