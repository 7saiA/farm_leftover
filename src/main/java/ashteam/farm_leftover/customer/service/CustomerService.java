package ashteam.farm_leftover.customer.service;

import ashteam.farm_leftover.customer.dto.CustomerDto;
import ashteam.farm_leftover.customer.dto.CustomerUpdatePasswordDto;
import ashteam.farm_leftover.customer.dto.NewCustomerDto;

public interface CustomerService {
    CustomerDto createCustomer(NewCustomerDto newCustomerDto);

    CustomerDto findCustomerById(Integer customerId);

    CustomerDto updateCustomer(Integer customerId, NewCustomerDto newCustomerDto);

    CustomerDto deleteCustomer(Integer customerId);

    CustomerDto changePassword(Integer customerId, CustomerUpdatePasswordDto customerUpdatePasswordDto);
}
