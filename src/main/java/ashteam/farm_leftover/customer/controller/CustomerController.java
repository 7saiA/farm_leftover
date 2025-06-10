package ashteam.farm_leftover.customer.controller;

import ashteam.farm_leftover.customer.dto.CustomerDto;
import ashteam.farm_leftover.customer.dto.NewCustomerDto;
import ashteam.farm_leftover.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    final CustomerService customerService;

    @PostMapping
    public CustomerDto createCustomer(@RequestBody NewCustomerDto newCustomerDto){
        return customerService.createCustomer(newCustomerDto);
    }

    @GetMapping("/{customerId}")
    public CustomerDto findCustomerById(@PathVariable String customerId){
        return customerService.findCustomerById(customerId);
    }

    @PutMapping("/{customerId}")
    public CustomerDto updateCustomer(@PathVariable String customerId, @RequestBody NewCustomerDto newCustomerDto){
        return customerService.updateCustomer(customerId,newCustomerDto);
    }

    @DeleteMapping("/{customerId}")
    public CustomerDto deleteCustomer(@PathVariable String customerId){
        return  customerService.deleteCustomer(customerId);
    }

}
