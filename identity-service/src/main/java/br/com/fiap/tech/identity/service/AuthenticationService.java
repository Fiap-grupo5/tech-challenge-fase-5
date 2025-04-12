package br.com.fiap.tech.identity.service;

import br.com.fiap.tech.identity.domain.User;
import br.com.fiap.tech.identity.domain.UserType;
import br.com.fiap.tech.identity.dto.AuthenticationRequest;
import br.com.fiap.tech.identity.dto.AuthenticationResponse;
import br.com.fiap.tech.identity.dto.RegisterRequest;
import br.com.fiap.tech.identity.events.UserCreatedEvent;
import br.com.fiap.tech.identity.repository.UserRepository;
import br.com.fiap.tech.identity.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StreamBridge streamBridge;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .userType(request.getUserType())
                .build();

        userRepository.save(user);
        
        // Publish user created event
        var userCreatedEvent = new UserCreatedEvent(
                user.getId(),
                user.getUsername(),
                user.getUserType()
        );
        streamBridge.send("userCreatedOutput-out-0", userCreatedEvent);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
