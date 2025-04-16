package br.com.fiap.tech.identity.service;

import br.com.fiap.tech.identity.domain.User;
import br.com.fiap.tech.identity.domain.UserType;
import br.com.fiap.tech.identity.dto.AuthenticationRequest;
import br.com.fiap.tech.identity.dto.AuthenticationResponse;
import br.com.fiap.tech.identity.dto.RegisterRequest;
import br.com.fiap.tech.identity.events.UserCreatedEvent;
import br.com.fiap.tech.identity.events.UserDeletionEvent;
import br.com.fiap.tech.identity.repository.UserRepository;
import br.com.fiap.tech.identity.security.JwtService;
import br.com.fiap.tech.identity.service.client.PeopleServiceClient;
import br.com.fiap.tech.identity.service.PeopleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StreamBridge streamBridge;
    private final PeopleServiceClient peopleServiceClient;
    private final PeopleService peopleService;
    private final UserCpfSyncService userCpfSyncService;

    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            StreamBridge streamBridge,
            @Lazy PeopleServiceClient peopleServiceClient,
            PeopleService peopleService,
            UserCpfSyncService userCpfSyncService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.streamBridge = streamBridge;
        this.peopleServiceClient = peopleServiceClient;
        this.peopleService = peopleService;
        this.userCpfSyncService = userCpfSyncService;
    }

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        log.info("Registering user with username: {} and CPF: {}", request.getUsername(), request.getCpf());

        if (userRepository.existsByUsername(request.getUsername())) {
            log.error("Username already exists.: {}", request.getUsername());
            throw new IllegalArgumentException("Username already exists.");
        }
        
        if (StringUtils.isEmpty(request.getFullName())) {
            log.error("Full name is mandatory.");
            throw new IllegalArgumentException("Full name is mandatory.");
        }
        if (StringUtils.isEmpty(request.getCpf())) {
            log.error("CPF is mandatory.");
            throw new IllegalArgumentException("CPF is mandatory.");
        }
        
        String normalizedCpf = request.getCpf().replaceAll("[^0-9]", "");
        if (normalizedCpf.length() < 11) {
            log.error("Invalid CPF: it must contain 11 digits.");
            throw new IllegalArgumentException("Invalid CPF: it must contain 11 digits.");
        }
        request.setCpf(normalizedCpf);
        
        if (StringUtils.isEmpty(request.getEmail())) {
            log.error("Email is mandatory.");
            throw new IllegalArgumentException("Email is mandatory.");
        }
        
        if (userCpfSyncService.checkCpfExists(request.getCpf())) {
            log.error("CPF already registered: {}", request.getCpf());
            throw new IllegalArgumentException("CPF already registered in system");
        }
        
        validateTypeSpecificFields(request);
        
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .userType(request.getUserType())
                .email(request.getEmail())
                .build();

        user = userRepository.save(user);
        log.info("User registered successfully: {}", user.getUsername());
        
        userCpfSyncService.registerCpf(request.getCpf(), user, user.getUserType(), user.getUsername());
        
        UserCreatedEvent userCreatedEvent = createUserEvent(user, request);
        boolean sent = false;
        
        try {
            log.info("Publishing user created event: {}", userCreatedEvent);
            sent = streamBridge.send("userCreatedOutput-out-0", userCreatedEvent);
            log.info("Event sent successfully: {}", sent);
        } catch (Exception e) {
            log.error("Failed to send user created event: {}", e.getMessage(), e);
            deleteUser(user.getId());
            throw new RuntimeException("Falha ao completar o processo de registro", e);
        }

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
    
    private void validateTypeSpecificFields(RegisterRequest request) {
        if (UserType.DOCTOR.equals(request.getUserType())) {
            if (StringUtils.isEmpty(request.getCrm())) {
                log.error("CRM is mandatory for doctors.");
                throw new IllegalArgumentException("CRM is mandatory for doctors.");
            }
            if (StringUtils.isEmpty(request.getSpecialty())) {
                log.error("Specialty is mandatory for doctors.");
                throw new IllegalArgumentException("Specialty is mandatory for doctors.");
            }
            
            boolean crmExists = false;
            try {
                crmExists = peopleServiceClient.checkCrmExists(request.getCrm());
                if (crmExists) {
                    log.error("CRM already registered: {}", request.getCrm());
                    throw new IllegalArgumentException("CRM already registered");
                }
            } catch (Exception e) {
                if (e instanceof IllegalArgumentException) {
                    throw e;
                }
                log.error("Error while verifying CRM: {}", e.getMessage());
                throw new RuntimeException("Unable to validate CRM. Please try again.");
            }
        } else if (UserType.PATIENT.equals(request.getUserType())) {
            if (StringUtils.isEmpty(request.getNationalHealthCard())) {
                log.error("National Health Card is mandatory for patients.");
                throw new IllegalArgumentException("National Health Card is mandatory for patients.");
            }
        }
    }

    private UserCreatedEvent createUserEvent(User user, RegisterRequest request) {
        String normalizedCpf = request.getCpf();
        if (normalizedCpf == null || normalizedCpf.trim().isEmpty()) {
            log.error("CPF is empty for user {}. This will cause the event processing to fail.", user.getUsername());
            normalizedCpf = "00000000000";
        } else {
            normalizedCpf = normalizedCpf.replaceAll("[^0-9]", "");
            if (normalizedCpf.length() > 11) {
                normalizedCpf = normalizedCpf.substring(0, 11);
            } else if (normalizedCpf.length() < 11) {
                // Padding com zeros à esquerda se necessário
                normalizedCpf = String.format("%011d", Long.parseLong(normalizedCpf));
            }
        }
        
        log.info("CPF normalized for event.: {}", normalizedCpf);
        
        return UserCreatedEvent.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .userType(user.getUserType().name())
                .fullName(request.getFullName())
                .cpf(normalizedCpf)
                .email(request.getEmail())
                .crm(request.getUserType() == UserType.DOCTOR ? request.getCrm() : null)
                .specialty(request.getUserType() == UserType.DOCTOR ? request.getSpecialty() : null)
                .nationalHealthCard(request.getUserType() == UserType.PATIENT ? request.getNationalHealthCard() : null)
                .phoneNumber(request.getPhoneNumber())
                .birthDate(request.getBirthDate())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .build();
    }
    
    public void deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        userRepository.findById(userId).ifPresent(user -> {
            userRepository.delete(user);
            log.info("User deleted with ID: {}", userId);
            
            try {
                UserDeletionEvent deletionEvent = new UserDeletionEvent(userId);
                boolean sent = streamBridge.send("userDeletedOutput-out-0", deletionEvent);
                log.info("User deletion event sent: {}", sent);
            } catch (Exception e) {
                log.error("Failed to send user deletion event: {}", e.getMessage(), e);
            }
        });
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
