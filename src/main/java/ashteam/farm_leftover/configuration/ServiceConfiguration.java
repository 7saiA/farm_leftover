package ashteam.farm_leftover.configuration;

import ashteam.farm_leftover.cart.dto.CartItemDto;
import ashteam.farm_leftover.cart.model.CartItem;
import ashteam.farm_leftover.order.model.OrderItem;
import ashteam.farm_leftover.product.dto.ProductDto;
import ashteam.farm_leftover.product.model.Product;
import ashteam.farm_leftover.user.dto.FarmDto;
import ashteam.farm_leftover.user.dto.UserDto;
import ashteam.farm_leftover.user.model.UserAccount;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ServiceConfiguration {

    @Bean
    ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.createTypeMap(UserAccount.class, UserDto.class);
        modelMapper.typeMap(UserAccount.class, FarmDto.class)
                .addMappings(mapper -> mapper.map(UserAccount::getProducts, FarmDto::setProducts));
        modelMapper.typeMap(Product.class, ProductDto.class)
                .addMappings(mapper ->
                        mapper.map(pf -> pf.getUserAccount().getFarmName(), ProductDto::setFarmName));
        modelMapper.typeMap(CartItem.class, OrderItem.class)
                        .addMappings(mapper -> {
                            mapper.map(CartItem::getProduct, OrderItem::setProduct);
                            mapper.map(pc -> pc.getProduct().getUnit(), OrderItem::setUnit);
                            mapper.map(pc -> pc.getProduct().getPricePerUnit(), OrderItem::setPricePerUnit);
                            mapper.map(pc -> pc.getProduct().getUserAccount().getFarmName(), OrderItem::setFarmName);
                        });

        modelMapper.typeMap(CartItem.class, CartItemDto.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getProduct().getProductId(), CartItemDto::setProductId);
                    mapper.map(src -> src.getProduct().getProductName(), CartItemDto::setProductName);
                    mapper.map(src -> src.getProduct().getPricePerUnit(), CartItemDto::setPricePerUnit);
                    mapper.map(src -> src.getProduct().getUnit(), CartItemDto::setUnit);
                });
        return modelMapper;
    }

    @Bean
    PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
}
