package ashteam.farm_leftover.order.model;

import ashteam.farm_leftover.product.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "orderItemId")
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long orderItemId;

    @Setter
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    Order order;

    @Setter
    String imgUrl;
    @Setter
    String productName;
    @Setter
    String unit;
    @Setter
    BigDecimal pricePerUnit;
    @Setter
    Integer quantity;
    @Setter
    BigDecimal subtotal;

    public static OrderItem fromCartItem(Product product, Integer quantity){
        OrderItem orderItem = new OrderItem();
        orderItem.setImgUrl(product.getImgUrl());
        orderItem.setProductName(product.getProductName());
        orderItem.setUnit(product.getUnit());
        orderItem.setPricePerUnit(product.getPricePerUnit());
        orderItem.setQuantity(quantity);
        orderItem.setSubtotal(product.getPricePerUnit().multiply(BigDecimal.valueOf(quantity)));
        return orderItem;
    }
}
