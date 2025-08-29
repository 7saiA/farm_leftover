package ashteam.farm_leftover.product.model;

import ashteam.farm_leftover.cart.model.CartItem;
import ashteam.farm_leftover.order.model.OrderItem;
import ashteam.farm_leftover.user.model.UserAccount;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "productId")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String productId;
    @Setter
    String productName;
    @Setter
    BigDecimal pricePerUnit;
    @Setter
    String unit;
    @Setter
    Integer availableQuantity;
    @Setter
    String imgUrl;
    @ManyToOne
    @JoinColumn(name = "user_account_id")
    UserAccount userAccount;
    @CreationTimestamp
    LocalDateTime createdAt;
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    List<CartItem> cartItems = new ArrayList<>();
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    List<OrderItem> orderItems = new ArrayList<>();

    public Product(String productName, BigDecimal pricePerUnit, String unit, Integer availableQuantity) {
        this.productName = productName;
        this.pricePerUnit = pricePerUnit;
        this.unit = unit;
        this.availableQuantity = availableQuantity;
    }

    public void setUser(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
}
