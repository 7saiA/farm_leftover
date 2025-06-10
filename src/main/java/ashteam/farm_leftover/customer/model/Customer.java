package ashteam.farm_leftover.customer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "customerId")
@Entity
public class Customer {
    @Id
    String customerId;
    @Setter
    String customerName;
    String email;
    @Setter
    String password;
    @Setter
    String phone;

    public Customer(String customerName, String email, String password, String phone) {
        this.customerName = customerName;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
}
