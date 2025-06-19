package com.example.customermanagement.service;

import com.example.customermanagement.constants.CustomerConstants;
import com.example.customermanagement.dto.CustomerRequest;
import com.example.customermanagement.dto.CustomerResponse;
import com.example.customermanagement.exception.ResourceNotFoundException;
import com.example.customermanagement.model.Customer;
import com.example.customermanagement.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);


    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        log.info("Creating new customer with email: {}", request.getEmail());
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAnnualSpend(request.getAnnualSpend());
        customer.setLastPurchaseDate(request.getLastPurchaseDate());
        Customer savedCustomer = customerRepository.save(customer);
        log.debug("Customer created successfully with ID: {}", savedCustomer.getId());
        return toResponse(savedCustomer);
    }

    @Override
    public CustomerResponse getCustomerById(UUID id) throws ResourceNotFoundException {
        log.info("Fetching customer with ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", id);
                    return new ResourceNotFoundException(CustomerConstants.CUSTOMER_NOT_FOUND + id);
                });
        CustomerResponse response = toResponse(customer);
        log.debug("Customer retrieved successfully with ID: {}", id);
        return response;
    }

    @Override
    public List<CustomerResponse> getCustomersByName(String name) {
        log.info("Fetching customers with name containing: {}", name);
        List<Customer> customers = customerRepository.findByName(name);
        log.debug("Found {} customers with name containing: {}", customers.size(), name);
        if (customers.isEmpty()) {
            throw new ResourceNotFoundException("No data found with this name : " + name);
        }
        return customers.stream()
                .map(customer -> {
                    CustomerResponse response = toResponse(customer);
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse getCustomerByEmail(String email) throws ResourceNotFoundException {
        log.info("Fetching customer with email: {}", email);
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Customer not found with email: {}", email);
                    return new ResourceNotFoundException("Customer not found with email: " + email);
                });
        CustomerResponse response = toResponse(customer);
        log.debug("Customer retrieved successfully with email: {}", email);
        return response;
    }

    @Override
    public CustomerResponse updateCustomer(UUID id, CustomerRequest request) throws ResourceNotFoundException {
        log.info("Updating customer with ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", id);
                    return new ResourceNotFoundException(CustomerConstants.CUSTOMER_NOT_FOUND + id);
                });
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAnnualSpend(request.getAnnualSpend());
        customer.setLastPurchaseDate(request.getLastPurchaseDate());

        log.debug("Customer updated successfully with ID: {}", id);
        return toResponse(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(UUID id) throws ResourceNotFoundException {
        log.info("Deleting customer with ID: {}", id);
        if (!customerRepository.existsById(id)) {
            log.error("Customer not found with ID: {}", id);
            throw new ResourceNotFoundException(CustomerConstants.CUSTOMER_NOT_FOUND + id);
        }
        customerRepository.deleteById(id);
        log.debug("Customer deleted successfully with ID: {}", id);
    }

    private CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setAnnualSpend(customer.getAnnualSpend());
        response.setLastPurchaseDate(customer.getLastPurchaseDate());
        response.calculateTier();
        return response;
    }
}