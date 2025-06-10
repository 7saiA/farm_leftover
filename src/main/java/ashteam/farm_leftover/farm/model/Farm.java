package ashteam.farm_leftover.farm.model;

import ashteam.farm_leftover.product.model.Product;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "farmId")
@Entity
public class Farm {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)")
    String farmId;
    @Setter
    String farmName;
    @Setter
    String email;
    @Setter
    String password;
    @Setter
    String city;
    @Setter
    String street;
    @Setter
    String phone;
    @Setter
    @OneToMany
    Set<Product> products;

    public Farm(String farmName, String email, String password, String city, String street, String phone, Set<Product> products) {
        this.farmName = farmName;
        this.email = email;
        this.password = password;
        this.city = city;
        this.street = street;
        this.phone = phone;
        this.products = products;
    }
}
