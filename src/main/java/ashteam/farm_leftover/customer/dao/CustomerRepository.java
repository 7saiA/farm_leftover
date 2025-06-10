package ashteam.farm_leftover.customer.dao;

import ashteam.farm_leftover.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,String> {
}
