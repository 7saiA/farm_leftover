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
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "orderItemId")
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    String productName;
    String unit;
    BigDecimal pricePerUnit;
    Integer quantity;
    BigDecimal subtotal;

    public static OrderItem fromCartItem(Product product, Integer quantity){
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setProductName(product.getProductName());
        orderItem.setUnit(product.getUnit());
        orderItem.setPricePerUnit(product.getPricePerUnit());
        orderItem.setQuantity(quantity);
        orderItem.setSubtotal(product.getPricePerUnit().multiply(BigDecimal.valueOf(quantity)));
        return orderItem;
    }
}
