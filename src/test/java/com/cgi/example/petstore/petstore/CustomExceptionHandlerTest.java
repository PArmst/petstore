package com.cgi.example.petstore.petstore;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class CustomExceptionHandlerTest {

    @Mock
    private WebRequest webRequest;

    @InjectMocks
    private CustomExceptionHandler customExceptionHandler;

    @BeforeEach
    void setUo() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerError() {
        Exception exception = new Exception("Test Exception");

        ResponseEntity<Object> responseEntity = customExceptionHandler.handleGenericException(exception, webRequest);
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();

        //assertions
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assert errorResponse != null;
        assertEquals("An error occurred while processing the request.", errorResponse.message());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.status());

    }

    @Test
    void handleConstraintViolationException_ShouldReturnBadRequest() {
        ConstraintViolationException exception = mock(ConstraintViolationException.class);

        ResponseEntity<Object> responseEntity = customExceptionHandler.handleConstraintViolationException(exception, webRequest);
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();

        //assertions
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assert errorResponse != null;
        assertEquals("Invalid input. Pet ID must be between 1 and 2000", errorResponse.message());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.status());
    }
}