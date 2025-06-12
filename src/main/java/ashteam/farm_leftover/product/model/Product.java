package ashteam.farm_leftover.product.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "productId")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer productId;
    Integer farmId;
    @Setter
    String productName;
    @Setter
    Double pricePerUnit;
    @Setter
    String unit;
    @Setter
    Integer availableQuantity;

    public Product(Integer farmId, String productName, Double pricePerUnit, String unit, Integer availableQuantity) {
        this.farmId = farmId;
        this.productName = productName;
        this.pricePerUnit = pricePerUnit;
        this.unit = unit;
        this.availableQuantity = availableQuantity;
    }
}
