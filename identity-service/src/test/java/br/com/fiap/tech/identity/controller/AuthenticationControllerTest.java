package br.com.fiap.tech.identity.controller;

import br.com.fiap.tech.identity.dto.AuthenticationRequest;
import br.com.fiap.tech.identity.dto.AuthenticationResponse;
import br.com.fiap.tech.identity.dto.RegisterRequest;
import br.com.fiap.tech.identity.exception.UserAlreadyExistsException;
import br.com.fiap.tech.identity.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    private RegisterRequest registerRequest;
    private AuthenticationRequest authRequest;
    private AuthenticationResponse authResponse;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole("PATIENT");

        authRequest = new AuthenticationRequest();
        authRequest.setUsername("testuser");
        authRequest.setPassword("password123");

        authResponse = new AuthenticationResponse();
        authResponse.setToken("jwt_token");
        authResponse.setRefreshToken("refresh_token");
    }

    @Test
    void register_Success() throws Exception {
        when(authenticationService.register(any(RegisterRequest.class)))
                .thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt_token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh_token"));
    }

    @Test
    void register_UserExists_Returns409() throws Exception {
        when(authenticationService.register(any(RegisterRequest.class)))
                .thenThrow(new UserAlreadyExistsException("Username already exists"));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void authenticate_Success() throws Exception {
        when(authenticationService.authenticate(any(AuthenticationRequest.class)))
                .thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt_token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh_token"));
    }

    @Test
    void refreshToken_Success() throws Exception {
        when(authenticationService.refreshToken(any(String.class)))
                .thenReturn("new_jwt_token");

        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .header("Authorization", "Bearer refresh_token"))
                .andExpect(status().isOk())
                .andExpect(content().string("new_jwt_token"));
    }
}
