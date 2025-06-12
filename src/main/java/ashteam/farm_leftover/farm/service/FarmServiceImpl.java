package ashteam.farm_leftover.farm.service;

import ashteam.farm_leftover.farm.dao.FarmRepository;
import ashteam.farm_leftover.farm.dto.FarmDto;
import ashteam.farm_leftover.farm.dto.FarmUpdatePasswordDto;
import ashteam.farm_leftover.farm.dto.NewFarmDto;
import ashteam.farm_leftover.farm.dto.exceptions.FarmIncorrectOldPasswordException;
import ashteam.farm_leftover.farm.dto.exceptions.FarmNotFoundException;
import ashteam.farm_leftover.farm.model.Farm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FarmServiceImpl implements FarmService {

    final FarmRepository farmRepository;
    final ModelMapper modelMapper;

    @Override
    public FarmDto createFarm(NewFarmDto newFarmDto) {
        String hashedPassword = BCrypt.hashpw(newFarmDto.getPassword(), BCrypt.gensalt(12));
        Farm farm = new Farm(newFarmDto.getFarmName(), newFarmDto.getEmail(), hashedPassword, newFarmDto.getCity(),
                newFarmDto.getStreet(), newFarmDto.getPhone(), newFarmDto.getProducts());
        farm = farmRepository.save(farm);
        return modelMapper.map(farm, FarmDto.class);
    }

    @Override
    public FarmDto findFarmById(Integer farmId) {
        Farm farm = farmRepository.findById(farmId).orElseThrow(() -> new FarmNotFoundException(farmId));
        return modelMapper.map(farm, FarmDto.class);
    }

    @Override
    public FarmDto updateFarm(Integer farmId, NewFarmDto newFarmDto) {
        Farm farm = farmRepository.findById(farmId).orElseThrow(() -> new FarmNotFoundException(farmId));
        if(newFarmDto.getFarmName() != null){
            farm.setFarmName(newFarmDto.getFarmName());
        }
        if(newFarmDto.getEmail() != null){
            farm.setEmail(newFarmDto.getEmail());
        }
        if(newFarmDto.getCity() != null){
            farm.setCity(newFarmDto.getCity());
        }
        if(newFarmDto.getStreet() != null){
            farm.setStreet(newFarmDto.getStreet());
        }
        if(newFarmDto.getPhone() != null){
            farm.setPhone(newFarmDto.getPhone());
        }
        if(newFarmDto.getProducts() != null){
            farm.setProducts(newFarmDto.getProducts());
        }
        farm = farmRepository.save(farm);
        return modelMapper.map(farm, FarmDto.class);
    }

    @Override
    public FarmDto deleteFarm(Integer farmId) {
        Farm farm = farmRepository.findById(farmId).orElseThrow(() -> new FarmNotFoundException(farmId));
        FarmDto dto = modelMapper.map(farm, FarmDto.class);
        farmRepository.deleteById(farmId);
        return dto;
    }

    @Override
    public FarmDto changePassword(Integer farmId, FarmUpdatePasswordDto farmUpdatePasswordDto) {
        Farm farm = farmRepository.findById(farmId)
                .orElseThrow(() -> new FarmNotFoundException(farmId));

        if (!BCrypt.checkpw(farmUpdatePasswordDto.getOldPassword(), farm.getPassword())) {
            throw new FarmIncorrectOldPasswordException(farmUpdatePasswordDto.getOldPassword());
        }

        farm.setPassword(BCrypt.hashpw(farmUpdatePasswordDto.getNewPassword(), BCrypt.gensalt(12)));
        farmRepository.save(farm);

        return modelMapper.map(farm, FarmDto.class);
    }
}
