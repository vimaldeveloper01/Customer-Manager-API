package com.example.customermanagement.service;


import com.example.customermanagement.dto.CustomerRequest;
import com.example.customermanagement.dto.CustomerResponse;
import com.example.customermanagement.exception.ResourceNotFoundException;
import com.example.customermanagement.model.Customer;
import com.example.customermanagement.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    private Customer customer;
    private CustomerRequest request;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        request = new CustomerRequest();
        request.setName("vimalkumar");
        request.setEmail("vimalkumar@gmail.com");
        request.setAnnualSpend(BigDecimal.valueOf(1000.70));
        request.setLastPurchaseDate(LocalDateTime.now());
        customer = new Customer();
        customer.setId(customerId);
        customer.setName("vimalkumar");
        customer.setEmail("vimalkumar@gmail.com");
        customer.setAnnualSpend(BigDecimal.valueOf(1000.70));
        customer.setLastPurchaseDate(LocalDateTime.now());
    }

    @Test
    void testCreateCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse response = customerService.createCustomer(request);

        assertEquals(request.getEmail(), response.getEmail());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testGetCustomerById_Success() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        CustomerResponse response = customerService.getCustomerById(customerId);

        assertEquals(customer.getName(), response.getName());
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                customerService.getCustomerById(customerId));
        assertTrue(ex.getMessage().contains(customerId.toString()));
    }

    @Test
    void testGetCustomersByName_Success() {
        when(customerRepository.findByName("vimal")).thenReturn(List.of(customer));

        List<CustomerResponse> responses = customerService.getCustomersByName("vimal");

        assertEquals(1, responses.size());
        assertEquals("vimalkumar", responses.get(0).getName());
    }

    @Test
    void testGetCustomersByName_NotFound() {
        when(customerRepository.findByName("anita")).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomersByName("anita"));
    }

    @Test
    void testGetCustomerByEmail_Success() {
        when(customerRepository.findByEmail("vimalkumar@example.com")).thenReturn(Optional.of(customer));

        CustomerResponse response = customerService.getCustomerByEmail("vimalkumar@example.com");

        assertEquals("vimalkumar", response.getName());
    }

    @Test
    void testGetCustomerByEmail_NotFound() {
        when(customerRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                customerService.getCustomerByEmail("notfound@example.com"));
    }

    @Test
    void testUpdateCustomer_Success() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse response = customerService.updateCustomer(customerId, request);

        assertEquals("vimalkumar", response.getName());
    }

    @Test
    void testUpdateCustomer_NotFound() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                customerService.updateCustomer(customerId, request));
    }

    @Test
    void testDeleteCustomer_Success() {
        when(customerRepository.existsById(customerId)).thenReturn(true);

        assertDoesNotThrow(() -> customerService.deleteCustomer(customerId));

        verify(customerRepository).deleteById(customerId);
    }

    @Test
    void testDeleteCustomer_NotFound() {
        when(customerRepository.existsById(customerId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                customerService.deleteCustomer(customerId));
    }
}
