package br.com.fiap.tech.identity.service;

import br.com.fiap.tech.identity.domain.User;
import br.com.fiap.tech.identity.domain.UserType;
import br.com.fiap.tech.identity.dto.AuthenticationRequest;
import br.com.fiap.tech.identity.dto.AuthenticationResponse;
import br.com.fiap.tech.identity.dto.RegisterRequest;
import br.com.fiap.tech.identity.events.UserCreatedEvent;
import br.com.fiap.tech.identity.repository.UserRepository;
import br.com.fiap.tech.identity.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StreamBridge streamBridge;

    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            StreamBridge streamBridge
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.streamBridge = streamBridge;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        log.info("Registering new user: {}", request.getUsername());
        
        // Verificar se o usuário já existe
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Validar campos obrigatórios comuns
        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (request.getCpf() == null || request.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF is required");
        }
        
        // Validar campos obrigatórios específicos por tipo
        if (request.getUserType() == UserType.DOCTOR) {
            if (request.getCrm() == null || request.getCrm().trim().isEmpty()) {
                throw new IllegalArgumentException("CRM is required for doctors");
            }
            if (request.getSpecialty() == null || request.getSpecialty().trim().isEmpty()) {
                throw new IllegalArgumentException("Specialty is required for doctors");
            }
        } else if (request.getUserType() == UserType.PATIENT) {
            if (request.getNationalHealthCard() == null || request.getNationalHealthCard().trim().isEmpty()) {
                throw new IllegalArgumentException("National Health Card is required for patients");
            }
        }
        
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .userType(request.getUserType())
                .build();

        user = userRepository.save(user);
        log.info("User saved with ID: {}", user.getId());
        
        // Publish user created event with additional fields
        var userCreatedEvent = UserCreatedEvent.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .userType(user.getUserType().name())
                .fullName(request.getFullName())
                .cpf(request.getCpf())
                .crm(request.getCrm())
                .specialty(request.getSpecialty())
                .nationalHealthCard(request.getNationalHealthCard())
                .phoneNumber(request.getPhoneNumber())
                .birthDate(request.getBirthDate())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .build();
                
        log.info("Publishing user created event: {}", userCreatedEvent);
        boolean sent = streamBridge.send("userCreatedOutput-out-0", userCreatedEvent);
        log.info("Event sent successfully: {}", sent);

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
