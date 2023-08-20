package com.immpresariat.ArtAgencyApp.exception;

import com.immpresariat.ArtAgencyApp.payload.ErrorDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTests {

    @Mock
    private WebRequest mockWebRequest;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        when(mockWebRequest.getDescription(false)).thenReturn("Mocked WebRequest Description");
    }


    @DisplayName("JUnit test for ResourceNotFoundException")
    @Test
    public void testResourceNotFoundException() {

        //given
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

        //when
        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleResourceNotFoundException(exception, mockWebRequest);

        //then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getTimestamp());
        assertEquals(response.getBody().getMessage(), exception.getMessage());
        assertEquals(response.getBody().getDetails(), mockWebRequest.getDescription(false));

    }

    @DisplayName("JUnit test for ResourceAlreadyExists exception")
    @Test
    public void testResourceAlreadyExistsException() {
        //given - precondition or setup
        ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException("Resource already exists");

        //when - action or the behavior that we are going to test
        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleResourceAlreadyExistsException(exception, mockWebRequest);

        //then - verify the output
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getTimestamp());
        assertEquals(response.getBody().getMessage(), exception.getMessage());
        assertEquals(response.getBody().getDetails(), mockWebRequest.getDescription(false));

    }

    @DisplayName("JUnit test handleGlobalException method")
    @Test
    public void testResourceAlreadyExists() {
        //given - precondition or setup
        Exception exception = new Exception("Exception");

        //when - action or the behavior that we are going to test
        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleGlobalException(exception, mockWebRequest);

        //then - verify the output
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getTimestamp());
        assertEquals(response.getBody().getMessage(), exception.getMessage());
        assertEquals(response.getBody().getDetails(), mockWebRequest.getDescription(false));

    }


    //pozostałe - global exception handler


}
