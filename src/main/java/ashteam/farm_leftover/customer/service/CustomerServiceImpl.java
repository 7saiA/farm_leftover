package ashteam.farm_leftover.customer.service;

import ashteam.farm_leftover.customer.dao.CustomerRepository;
import ashteam.farm_leftover.customer.dto.CustomerDto;
import ashteam.farm_leftover.customer.dto.CustomerUpdatePasswordDto;
import ashteam.farm_leftover.customer.dto.NewCustomerDto;
import ashteam.farm_leftover.customer.dto.exceptions.CustomerIncorrectOldPassword;
import ashteam.farm_leftover.customer.dto.exceptions.CustomerNotFoundException;
import ashteam.farm_leftover.customer.model.Customer;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    final CustomerRepository customerRepository;
    final ModelMapper modelMapper;

    @Override
    public CustomerDto createCustomer(NewCustomerDto newCustomerDto) {
        String hashedPassword = BCrypt.hashpw(newCustomerDto.getPassword(),BCrypt.gensalt(12));
        Customer customer = new Customer(newCustomerDto.getCustomerName(),newCustomerDto.getEmail(),
                hashedPassword,newCustomerDto.getPhone());
        customer = customerRepository.save(customer);
        return modelMapper.map(customer,CustomerDto.class);
    }

    @Override
    public CustomerDto findCustomerById(Integer customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException(customerId));
        return modelMapper.map(customer,CustomerDto.class);
    }

    @Override
    public CustomerDto updateCustomer(Integer customerId, NewCustomerDto newCustomerDto) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException(customerId));
        if(newCustomerDto.getCustomerName() != null){
            customer.setCustomerName(newCustomerDto.getCustomerName());
        }
        if(newCustomerDto.getEmail() != null){
            customer.setEmail(newCustomerDto.getEmail());
        }
        if(newCustomerDto.getPassword() != null){
            customer.setPassword(newCustomerDto.getPassword());
        }
        if(newCustomerDto.getPhone() != null){
            customer.setPhone(newCustomerDto.getPhone());
        }
        customerRepository.save(customer);
        return modelMapper.map(customer,CustomerDto.class);
    }

    @Override
    public CustomerDto deleteCustomer(Integer customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException(customerId));
        customerRepository.deleteById(customerId);
        return modelMapper.map(customer,CustomerDto.class);
    }

    @Override
    public CustomerDto changePassword(Integer customerId, CustomerUpdatePasswordDto customerUpdatePasswordDto) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException(customerId));

        if(!BCrypt.checkpw(customerUpdatePasswordDto.getOldPassword(),customer.getPassword())){
            throw new CustomerIncorrectOldPassword(customerUpdatePasswordDto.getOldPassword());
        }
        customer.setPassword(BCrypt.hashpw(customerUpdatePasswordDto.getNewPassword(),BCrypt.gensalt(12)));
        customerRepository.save(customer);
        return modelMapper.map(customer,CustomerDto.class);
    }
}
