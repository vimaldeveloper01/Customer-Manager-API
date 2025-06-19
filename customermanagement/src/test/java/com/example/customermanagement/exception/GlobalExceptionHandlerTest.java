package com.example.customermanagement.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Customer not found");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<Object> response = handler.handleResourceNotFoundException(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(((LinkedHashMap<?, ?>) response.getBody()).get("message").toString().contains("Customer not found"));
    }

    @Test
    void testHandleMethodArgumentNotValid() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        List<FieldError> fieldErrors = List.of(new FieldError("customer", "email", "must not be blank"));
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Object> response = handler.handleMethodArgumentNotValid(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(((LinkedHashMap<?, ?>) response.getBody()).get("errors").toString().contains("email: must not be blank"));
    }

    @Test
    void testHandleDuplicateEmail() {
        ResponseEntity<String> response = handler.handleDuplicateEmail();

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email already exists", response.getBody());
    }

    @Test
    void testHandleGlobalException() {
        Exception ex = new RuntimeException("Unexpected error");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<Object> response = handler.handleGlobalException(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(((LinkedHashMap<?, ?>) response.getBody()).get("message").toString().contains("Unexpected error"));
    }
}
