package ashteam.farm_leftover.customer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)")
    String customerId;
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
