package br.com.fiap.tech.identity.service;

import br.com.fiap.tech.identity.dto.AuthenticationRequest;
import br.com.fiap.tech.identity.dto.AuthenticationResponse;
import br.com.fiap.tech.identity.dto.RegisterRequest;
import br.com.fiap.tech.identity.exception.UserAlreadyExistsException;
import br.com.fiap.tech.identity.model.User;
import br.com.fiap.tech.identity.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterRequest registerRequest;
    private AuthenticationRequest authRequest;
    private User user;

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

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("encoded_password");
        user.setRole("PATIENT");
    }

    @Test
    void register_Success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt_token");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refresh_token");

        AuthenticationResponse response = authenticationService.register(registerRequest);

        assertNotNull(response);
        assertEquals("jwt_token", response.getToken());
        assertEquals("refresh_token", response.getRefreshToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_UserExists_ThrowsException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> {
            authenticationService.register(registerRequest);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticate_Success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt_token");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refresh_token");

        AuthenticationResponse response = authenticationService.authenticate(authRequest);

        assertNotNull(response);
        assertEquals("jwt_token", response.getToken());
        assertEquals("refresh_token", response.getRefreshToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void refreshToken_Success() {
        when(jwtService.extractUsername(anyString())).thenReturn("testuser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("new_jwt_token");

        String newToken = authenticationService.refreshToken("valid_refresh_token");

        assertNotNull(newToken);
        assertEquals("new_jwt_token", newToken);
    }
}
