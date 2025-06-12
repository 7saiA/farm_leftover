package ashteam.farm_leftover.customer.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "customerId")
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer customerId;
    @Setter
    String customerName;
    @Setter
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
