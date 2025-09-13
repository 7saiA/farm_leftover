package ashteam.farm_leftover.configuration;

import ashteam.farm_leftover.cart.dto.CartItemDto;
import ashteam.farm_leftover.cart.model.CartItem;
import ashteam.farm_leftover.order.dto.OrderItemDto;
import ashteam.farm_leftover.order.dto.OrderResponseDto;
import ashteam.farm_leftover.order.model.Order;
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

        modelMapper.typeMap(CartItem.class, CartItemDto.class)
                .addMappings(mapper -> {
                    mapper.map(cartItem -> cartItem.getProduct().getProductId(), CartItemDto::setProductId);
                    mapper.map(cartItem -> cartItem.getProduct().getProductName(), CartItemDto::setProductName);
                    mapper.map(cartItem -> cartItem.getProduct().getPricePerUnit(), CartItemDto::setPricePerUnit);
                    mapper.map(cartItem -> cartItem.getProduct().getUnit(), CartItemDto::setUnit);
                });

        modelMapper.typeMap(Order.class, OrderResponseDto.class)
                .addMappings(mapper -> {
                   mapper.map(order -> order.getUser().getUserName(), OrderResponseDto::setUsername);
                   mapper.map(order -> order.getUser().getPhone(), OrderResponseDto::setUserPhone);
                   mapper.map(order -> order.getFarm().getFarmName(), OrderResponseDto::setFarmName);
                   mapper.map(order -> order.getFarm().getCity(), OrderResponseDto::setCity);
                   mapper.map(order -> order.getFarm().getStreet(), OrderResponseDto::setStreet);
                   mapper.map(Order::getCancellationReason,OrderResponseDto::setCancellationReason);
                   mapper.map(Order::getItems, OrderResponseDto::setItems);
                });

        return modelMapper;
    }

    @Bean
    PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
}
