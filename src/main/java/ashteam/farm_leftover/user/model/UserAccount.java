package ashteam.farm_leftover.user.model;

import ashteam.farm_leftover.product.model.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "login")
public class UserAccount {
    @Id
    @Setter
    String login;
    @Setter
    String email;
    @Setter
    String password;
    @Setter
    String phone;
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    Set<Role> roles = new HashSet<>();

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

    public UserAccount(String login, String email, String password, String phone) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.phone = phone;
        roles.add(Role.USER);
    }

    //For Farm Role
    public UserAccount(String login, String email, String password, String phone, String farmName, String city, String street) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.phone = phone;
        roles.add(Role.FARM);
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

    public void removeProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product can't be null");
        }
        products.remove(product);
        product.setUser(null);
    }

    public boolean addRole(String role){
        return roles.add(Role.valueOf(role.toUpperCase()));
    }

    public boolean removeRole(String role){
        return roles.remove(Role.valueOf(role.toUpperCase()));
    }
}
