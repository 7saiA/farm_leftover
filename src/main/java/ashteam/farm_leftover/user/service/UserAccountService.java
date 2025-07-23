package ashteam.farm_leftover.user.service;

import ashteam.farm_leftover.user.dto.*;

public interface UserAccountService {
    UserDto updateUser(String login, UpdateUserDto updateUserDto);
    UserDto deleteUser(String login);
    UserProfileDto getUser(String login);
    Iterable<FarmDto> getAllFarms();
    FarmDto findFarmByFarmName(String farmName);
    Iterable<FarmDto> searchFarms(String query);
}
