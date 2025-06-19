package com.example.customermanagement.controller;

import com.example.customermanagement.dto.CustomerRequest;
import com.example.customermanagement.dto.CustomerResponse;
import com.example.customermanagement.exception.ResourceNotFoundException;
import com.example.customermanagement.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private CustomerRequest customerRequest;
    private CustomerResponse customerResponse;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerId = UUID.randomUUID();
        customerRequest = new CustomerRequest();
        customerRequest.setName("John Doe");
        customerRequest.setEmail("john@example.com");
        customerRequest.setAnnualSpend(new BigDecimal("2000"));
        customerRequest.setLastPurchaseDate(LocalDateTime.now().minusMonths(3));

        customerResponse = new CustomerResponse();
        customerResponse.setId(customerId);
        customerResponse.setName("John Doe");
        customerResponse.setEmail("john@example.com");
        customerResponse.setAnnualSpend(new BigDecimal("2000"));
        customerResponse.setLastPurchaseDate(customerRequest.getLastPurchaseDate());
        customerResponse.calculateTier();
    }

    @Test
    void testCreateCustomer_Success() {
        when(customerService.createCustomer(customerRequest)).thenReturn(customerResponse);

        ResponseEntity<CustomerResponse> response = customerController.createCustomer(customerRequest);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(customerResponse.getId(), response.getBody().getId());
        verify(customerService, times(1)).createCustomer(customerRequest);
    }

    @Test
    void testGetCustomerById_Success() {
        when(customerService.getCustomerById(customerId)).thenReturn(customerResponse);

        ResponseEntity<CustomerResponse> response = customerController.getCustomerById(customerId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(customerId, response.getBody().getId());
        verify(customerService, times(1)).getCustomerById(customerId);
    }


    @Test
    void testSearchCustomerByName_Found() {
        when(customerService.getCustomersByName("John")).thenReturn(List.of(customerResponse));

        ResponseEntity<?> response = customerController.searchCustomers("John", null);

        assertEquals(200, response.getStatusCodeValue());
        verify(customerService).getCustomersByName("John");
    }

    @Test
    void testSearchCustomerByEmail_Found() {
        when(customerService.getCustomerByEmail("john@example.com")).thenReturn(customerResponse);

        ResponseEntity<?> response = customerController.searchCustomers(null, "john@example.com");

        assertEquals(200, response.getStatusCodeValue());
        verify(customerService).getCustomerByEmail("john@example.com");
    }

    @Test
    void testSearchCustomer_MissingParams() {
        ResponseEntity<?> response = customerController.searchCustomers(null, null);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Either name or email"));
    }

    @Test
    void testUpdateCustomer_Success() {
        when(customerService.updateCustomer(customerId, customerRequest)).thenReturn(customerResponse);

        ResponseEntity<CustomerResponse> response = customerController.updateCustomer(customerId, customerRequest);

        assertEquals(200, response.getStatusCodeValue());
        verify(customerService).updateCustomer(customerId, customerRequest);
    }


    @Test
    void testDeleteCustomer_Success() {
        when(customerService.getCustomerById(customerId)).thenReturn(customerResponse);

        ResponseEntity<String> response = customerController.deleteCustomer(customerId);

        assertEquals(204, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("deleted successfully"));
        verify(customerService).deleteCustomer(customerId);
    }

    @Test
    void testDeleteCustomer_NotFound() {
        when(customerService.getCustomerById(customerId)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> customerController.deleteCustomer(customerId));
    }
}
