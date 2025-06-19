package com.example.customermanagement.controller;

import com.example.customermanagement.dto.CustomerRequest;
import com.example.customermanagement.dto.CustomerResponse;
import com.example.customermanagement.exception.ResourceNotFoundException;
import com.example.customermanagement.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
@Tag(name = "Customer Management", description = "API for managing customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    @PostMapping
    @Operation(summary = "Create a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
        log.info("Received request to create customer with email: {}", customerRequest.getEmail());
        CustomerResponse response = customerService.createCustomer(customerRequest);
        log.debug("Customer created with ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CustomerResponse> getCustomerById(
            @Parameter(description = "ID of the customer to be retrieved") @PathVariable UUID id) throws ResourceNotFoundException {
        log.info("Received request to get customer with ID: {}", id);
        CustomerResponse response = customerService.getCustomerById(id);
        log.debug("Returning customer with ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Search customers by name or email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers found"),
            @ApiResponse(responseCode = "404", description = "No customers found")
    })
    public ResponseEntity<?> searchCustomers(
            @Parameter(description = "Name to search for") @RequestParam(required = false) String name,
            @Parameter(description = "Email to search for") @RequestParam(required = false) String email) throws ResourceNotFoundException {

        if (name != null) {
            log.info("Received request to search customers by name: {}", name);
            List<CustomerResponse> responses = customerService.getCustomersByName(name);
            log.debug("Returning {} customers with name containing: {}", responses.size(), name);
            return ResponseEntity.ok(responses);
        } else if (email != null) {
            log.info("Received request to search customer by email: {}", email);
            CustomerResponse response = customerService.getCustomerByEmail(email);
            log.debug("Returning customer with email: {}", email);
            return ResponseEntity.ok(response);
        } else {
            log.warn("Received search request without name or email parameters");
            return ResponseEntity.badRequest().body("Either name or email parameter must be provided");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CustomerResponse> updateCustomer(
            @Parameter(description = "ID of the customer to be updated") @PathVariable UUID id,
            @Valid @RequestBody CustomerRequest customerRequest) throws ResourceNotFoundException {
        log.info("Received request to update customer with ID: {}", id);
        CustomerResponse response = customerService.updateCustomer(id, customerRequest);
        log.debug("Customer with ID: {} updated successfully", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<String> deleteCustomer(
            @Parameter(description = "ID of the customer to be deleted") @PathVariable UUID id) throws ResourceNotFoundException {
        log.info("Received request to delete customer with ID: {}", id);
        CustomerResponse response = customerService.getCustomerById(id);
        log.debug("Returning customer with ID: {}", id);
        if (response == null) {
            log.debug("No customers found with ID containing: {}", id);
            throw new ResourceNotFoundException("No customers found with ID: " + id);
        } else {
            customerService.deleteCustomer(id);
        }
        log.debug("Customer with ID: {} deleted successfully", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Customer with ID: " + id + " deleted successfully");
    }
}