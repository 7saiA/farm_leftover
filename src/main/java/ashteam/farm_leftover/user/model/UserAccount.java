package ashteam.farm_leftover.user.model;

import ashteam.farm_leftover.product.model.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Setter
    String name;
    @Setter
    String email;
    @Setter
    String password;
    @Setter
    String phone;
    @Enumerated(EnumType.STRING)
    RoleUser roleUser;

    //For Farm Role
    @Setter
    String farmName;
    @Setter
    String city;
    @Setter
    String street;
    @Setter
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Product> products = new HashSet<>();

    public UserAccount(String name, String email, String password, String phone, RoleUser roleUser) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.roleUser = roleUser;
    }

    //For Farm Role
    public UserAccount(String name, String email, String password, String phone, RoleUser roleUser, String farmName, String city, String street, Set<Product> products) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.roleUser = roleUser;
        this.farmName = farmName;
        this.city = city;
        this.street = street;
        this.products = products;
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product can't be null");
        }
        products.add(product);
        product.setUser(this);
    }

    public void removeProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product can't be null");
        }
        products.remove(product);
        product.setUser(null);
    }
}
