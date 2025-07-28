package ashteam.farm_leftover.user.service;

import ashteam.farm_leftover.user.dto.*;

public interface UserAccountService {
    UserDto updateUser(String login, UpdateUserDto updateUserDto);
    void deleteUser(String login);
    UserDto getUser(String login);
    FarmDto findFarmByFarmName(String farmName);
    Iterable<FarmDto> getAllFarms();
    Iterable<FarmForSearchDto> searchFarms(String query);
}
