package ashteam.farm_leftover.user.service;

import ashteam.farm_leftover.geocoding.service.GeocodingService;
import ashteam.farm_leftover.jwt.service.JwtTokenService;
import ashteam.farm_leftover.user.dao.UserAccountRepository;
import ashteam.farm_leftover.user.dto.*;
import ashteam.farm_leftover.user.dto.exceptions.FarmNotFoundException;
import ashteam.farm_leftover.user.dto.exceptions.UserNotFoundException;
import ashteam.farm_leftover.user.model.Role;
import ashteam.farm_leftover.user.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {
    private static final Logger log = LogManager.getLogger(UserAccountServiceImpl.class);

    final UserAccountRepository userAccountRepository;
    final ModelMapper modelMapper;
    final JwtTokenService jwtTokenService;
    final GeocodingService geocodingService;

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
    public void deleteUser(String login) {
        jwtTokenService.revokeAccessToken(login);
        jwtTokenService.revokeRefreshToken(login);
        userAccountRepository.deleteById(login);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUser(String login) {
        UserAccount user = userAccountRepository.findById(login)
                .orElseThrow(() -> new UserNotFoundException(login));
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public FarmDto findFarmByFarmName(String farmName) {
        log.info("Searching for farm: {}", farmName);
        UserAccount farm = userAccountRepository.findByFarmName(farmName).
                orElseThrow(() -> new FarmNotFoundException(farmName));
        log.info("Found farm: {}", farm.getFarmName());
        return modelMapper.map(farm, FarmDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<AllFarmsDto> getAllFarms() {
        return userAccountRepository.findAll().stream()
                .filter(u -> u.getRole().equals(Role.FARM))
                .map(f -> modelMapper.map(f, AllFarmsDto.class))
                .toList();
    }

    @Transactional
    @Override
    public Iterable<FarmForSearchDto> searchFarms(String query) {
        return userAccountRepository.findUserAccountByRoleAndFarmNameContainsIgnoreCase(Role.FARM,query)
                .stream()
                .map(f -> modelMapper.map(f, FarmForSearchDto.class))
                .toList();
    }

    @Transactional
    @Override
    public CoordinatesDto setFarmCoordinates(String login) {
        UserAccount farm = userAccountRepository.findById(login)
                .orElseThrow(() -> new UserNotFoundException(login));

        double[] coordinates = geocodingService.getCoordinates(farm.getCity(), farm.getStreet());
        CoordinatesDto coordinatesDto = new CoordinatesDto(coordinates[0],coordinates[1]);
        farm.setLatitude(coordinatesDto.getLat());
        farm.setLongitude(coordinatesDto.getLng());
        userAccountRepository.save(farm);
        return coordinatesDto;
    }
}
