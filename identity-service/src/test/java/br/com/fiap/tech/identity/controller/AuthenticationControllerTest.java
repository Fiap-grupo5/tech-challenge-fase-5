package br.com.fiap.tech.identity.controller;

import br.com.fiap.tech.identity.domain.User;
import br.com.fiap.tech.identity.domain.UserType;
import br.com.fiap.tech.identity.dto.AdministratorDTO;
import br.com.fiap.tech.identity.dto.AuthenticationRequest;
import br.com.fiap.tech.identity.dto.AuthenticationResponse;
import br.com.fiap.tech.identity.dto.RegisterRequest;
import br.com.fiap.tech.identity.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;


    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest();
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken("jwtToken");
        response.setUsername("newUser");

        when(authenticationService.register(request)).thenReturn(response);

        ResponseEntity<?> result = authenticationController.register(request);
        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void testAuthenticateSuccess() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("mikasa");
        request.setPassword("pass");

        AuthenticationResponse mockResponse = new AuthenticationResponse();
        mockResponse.setToken("token123");
        mockResponse.setUsername("mikasa");

        when(authenticationService.authenticate(request)).thenReturn(mockResponse);

        ResponseEntity<?> response = authenticationController.authenticate(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testAdministratorExistsTrue() {
        when(authenticationService.checkAdministratorExists(1L)).thenReturn(true);
        ResponseEntity<Boolean> response = authenticationController.administratorExists(1L);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());
    }

    @Test
    void testAdministratorExistsFalse() {
        when(authenticationService.checkAdministratorExists(2L)).thenThrow(new RuntimeException("error"));
        ResponseEntity<Boolean> response = authenticationController.administratorExists(2L);
        assertEquals(200, response.getStatusCode().value());
        assertNotEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    void testGetAdministratorFound() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setEmail("admin@example.com");
        user.setUserType(UserType.ADMINISTRATOR);

        when(authenticationService.getUserById(1L)).thenReturn(user);

        ResponseEntity<AdministratorDTO> response = authenticationController.getAdministrator(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("admin", response.getBody().getUsername());
    }

    @Test
    void testGetAdministratorNotFound() {
        when(authenticationService.getUserById(2L)).thenReturn(null);
        ResponseEntity<AdministratorDTO> response = authenticationController.getAdministrator(2L);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testRegisterConflict() {
        RegisterRequest request = new RegisterRequest();
        when(authenticationService.register(request))
                .thenThrow(new RuntimeException("User already exists"));

        ResponseEntity<?> result = authenticationController.register(request);

        assertEquals(409, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().toString().contains("User already exists"));
    }

    @Test
    void testRegisterInternalServerError() {
        RegisterRequest request = new RegisterRequest();
        when(authenticationService.register(request))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> result = authenticationController.register(request);

        assertEquals(500, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().toString().contains("An unexpected error occurred"));
    }

    @Test
    void testAuthenticateUnauthorized() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("mikasa");
        request.setPassword("wrongPass");

        when(authenticationService.authenticate(request))
                .thenThrow(new RuntimeException("Invalid credentials"));

        ResponseEntity<?> response = authenticationController.authenticate(request);

        assertEquals(401, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("Invalid credentials"));
    }
}
