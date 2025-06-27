package ashteam.farm_leftover.configuration;

import ashteam.farm_leftover.product.dto.ProductDto;
import ashteam.farm_leftover.product.model.Product;
import ashteam.farm_leftover.user.dto.UserForProductDto;
import ashteam.farm_leftover.user.model.UserAccount;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        // Маппинг из UserAccount (entity) в UserForProductDto (DTO)
        modelMapper.createTypeMap(UserAccount.class, UserForProductDto.class);

        // Маппинг из Product в ProductDto с учетом преобразования UserAccount в UserForProductDto
        modelMapper.typeMap(Product.class, ProductDto.class)
                .addMappings(mapper -> mapper.map(Product::getUserAccount, ProductDto::setUserForProductDto));
        return modelMapper;
    }
}

//For Future
//modelMapper.typeMap(FarmDto.class, UserAccount.class)
//            .addMapping(FarmDto::getFarmName, UserAccount::setName);
//
//    modelMapper.typeMap(UserAccount.class, FarmDto.class)
//            .addMapping(UserAccount::getName, FarmDto::setFarmName);