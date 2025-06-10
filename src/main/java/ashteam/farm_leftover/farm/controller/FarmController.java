package ashteam.farm_leftover.farm.controller;

import ashteam.farm_leftover.farm.dto.FarmDto;
import ashteam.farm_leftover.farm.dto.NewFarmDto;
import ashteam.farm_leftover.farm.service.FarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/farms")
public class FarmController {

    final FarmService farmService;

    @PostMapping
    public FarmDto createFarm(@RequestBody NewFarmDto newFarmDto) {
        return farmService.createFarm(newFarmDto);
    }

    @GetMapping("/{farmId}")
    public FarmDto findFarmById(@PathVariable String farmId) {
        return farmService.findFarmById(farmId);
    }

    @PutMapping("/{farmId}")
    public FarmDto updateFarm(@PathVariable String farmId, @RequestBody NewFarmDto newFarmDto) {
        return farmService.updateFarm(farmId, newFarmDto);
    }

    @DeleteMapping("/{farmId}")
    public FarmDto deleteFarm(@PathVariable String farmId) {
        return farmService.deleteFarm(farmId);
    }
}
