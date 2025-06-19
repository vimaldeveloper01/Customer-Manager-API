package com.example.customermanagement.exception;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceNotFoundExceptionTest {

    @Test
    void testMessageConstructor() {
        String message = "Customer not found";
        ResourceNotFoundException ex = new ResourceNotFoundException(message);

        assertEquals(message, ex.getMessage());
    }

    @Test
    void testDetailedConstructor() {
        String resourceName = "Customer";
        String fieldName = "id";
        int fieldValue = 123;

        ResourceNotFoundException ex = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

        assertEquals("Customer not found with id : '123'", ex.getMessage());
    }
}
