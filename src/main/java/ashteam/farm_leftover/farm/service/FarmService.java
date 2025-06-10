package ashteam.farm_leftover.farm.service;

import ashteam.farm_leftover.farm.dto.FarmDto;
import ashteam.farm_leftover.farm.dto.NewFarmDto;

public interface FarmService {
    FarmDto createFarm(NewFarmDto newFarmDto);

    FarmDto findFarmById(String farmId);

    FarmDto updateFarm(String farmId, NewFarmDto newFarmDto);

    FarmDto deleteFarm(String farmId);
}
