package ashteam.farm_leftover.farm.controller;

import ashteam.farm_leftover.farm.dto.FarmDto;
import ashteam.farm_leftover.farm.dto.FarmUpdatePasswordDto;
import ashteam.farm_leftover.farm.dto.NewFarmDto;
import ashteam.farm_leftover.farm.service.FarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
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
    public FarmDto findFarmById(@PathVariable Integer farmId) {
        return farmService.findFarmById(farmId);
    }

    @PutMapping("/{farmId}")
    public FarmDto updateFarm(@PathVariable Integer farmId, @RequestBody NewFarmDto newFarmDto) {
        return farmService.updateFarm(farmId, newFarmDto);
    }

    @PatchMapping("/{farmId}")
    public FarmDto changePassword(@PathVariable Integer farmId, @RequestBody FarmUpdatePasswordDto farmUpdatePasswordDto){
        return farmService.changePassword(farmId,farmUpdatePasswordDto);
    }

    @DeleteMapping("/{farmId}")
    public FarmDto deleteFarm(@PathVariable Integer farmId) {
        return farmService.deleteFarm(farmId);
    }
}
