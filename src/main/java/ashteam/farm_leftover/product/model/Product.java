package ashteam.farm_leftover.product.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "productId")
@Entity
public class Product {
    @Id
    String productId;
    String farmId;
    @Setter
    String productName;
    @Setter
    Double pricePerUnit;
    @Setter
    String unit;
    @Setter
    Integer availableQuantity;

    public Product(String farmId, String productName, Double pricePerUnit, String unit, Integer availableQuantity) {
        this.farmId = farmId;
        this.productName = productName;
        this.pricePerUnit = pricePerUnit;
        this.unit = unit;
        this.availableQuantity = availableQuantity;
    }
}
