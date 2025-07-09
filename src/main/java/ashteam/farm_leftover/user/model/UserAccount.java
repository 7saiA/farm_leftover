package ashteam.farm_leftover.user.model;

import ashteam.farm_leftover.product.model.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Setter
@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "login")
public class UserAccount {
    @Id
    String login;
    String email;
    String password;
    String phone;
    @Enumerated(EnumType.STRING)
    Role role;

    String farmName;
    String city;
    String street;
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Product> products = new HashSet<>();

    public UserAccount(String login, String email, String password, String phone) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = Role.USER;
    }

    //For Farm Role
    public UserAccount(String login, String email, String password, String phone, String farmName, String city, String street) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = Role.FARM;
        this.farmName = farmName;
        this.city = city;
        this.street = street;
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product can't be null");
        }
        products.add(product);
        product.setUser(this);
    }

    public void changeRoleToFarm() {
        this.role = Role.FARM;
    }
}
