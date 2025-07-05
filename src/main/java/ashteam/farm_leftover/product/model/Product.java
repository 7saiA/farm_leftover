package ashteam.farm_leftover.product.model;

import ashteam.farm_leftover.user.model.UserAccount;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "productId")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long productId;
    @Setter
    String productName;
    @Setter
    Double pricePerUnit;
    @Setter
    String unit;
    @Setter
    Integer availableQuantity;
    @ManyToOne
    @JoinColumn(name = "user_account_id")
    UserAccount userAccount;

    @CreationTimestamp
    LocalDateTime createdAt;

    public Product(String productName, Double pricePerUnit, String unit, Integer availableQuantity) {
        this.productName = productName;
        this.pricePerUnit = pricePerUnit;
        this.unit = unit;
        this.availableQuantity = availableQuantity;
    }

    public void setUser(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
}
