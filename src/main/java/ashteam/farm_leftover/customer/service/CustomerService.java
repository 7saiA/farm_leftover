package ashteam.farm_leftover.customer.service;

import ashteam.farm_leftover.customer.dto.CustomerDto;
import ashteam.farm_leftover.customer.dto.NewCustomerDto;

public interface CustomerService {
    CustomerDto createCustomer(NewCustomerDto newCustomerDto);

    CustomerDto findCustomerById(String customerId);

    CustomerDto updateCustomer(String customerId, NewCustomerDto newCustomerDto);

    CustomerDto deleteCustomer(String customerId);
}
