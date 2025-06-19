package com.example.customermanagement.service;

import com.example.customermanagement.dto.CustomerRequest;
import com.example.customermanagement.dto.CustomerResponse;
import com.example.customermanagement.exception.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest customerRequest);

    CustomerResponse getCustomerById(UUID id) throws ResourceNotFoundException;

    List<CustomerResponse> getCustomersByName(String name);

    CustomerResponse getCustomerByEmail(String email) throws ResourceNotFoundException;

    CustomerResponse updateCustomer(UUID id, CustomerRequest customerRequest) throws ResourceNotFoundException;

    void deleteCustomer(UUID id) throws ResourceNotFoundException;
}