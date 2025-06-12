package ashteam.farm_leftover.farm.service;

import ashteam.farm_leftover.farm.dto.FarmDto;
import ashteam.farm_leftover.farm.dto.FarmUpdatePasswordDto;
import ashteam.farm_leftover.farm.dto.NewFarmDto;

public interface FarmService {
    FarmDto createFarm(NewFarmDto newFarmDto);

    FarmDto findFarmById(Integer farmId);

    FarmDto updateFarm(Integer farmId, NewFarmDto newFarmDto);

    FarmDto deleteFarm(Integer farmId);

    FarmDto changePassword(Integer farmId, FarmUpdatePasswordDto farmUpdatePasswordDto);
}
