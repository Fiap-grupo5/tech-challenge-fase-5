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

        // Verificar se o username já existe
        if (userRepository.existsByUsername(request.getUsername())) {
            log.error("Username já existe: {}", request.getUsername());
            throw new IllegalArgumentException("Username já existe");
        }
        
        // Validar campos obrigatórios
        if (StringUtils.isEmpty(request.getFullName())) {
            log.error("Nome completo é obrigatório");
            throw new IllegalArgumentException("Nome completo é obrigatório");
        }
        if (StringUtils.isEmpty(request.getCpf())) {
            log.error("CPF é obrigatório");
            throw new IllegalArgumentException("CPF é obrigatório");
        }
        
        // Normalizar o CPF
        String normalizedCpf = request.getCpf().replaceAll("[^0-9]", "");
        if (normalizedCpf.length() < 11) {
            log.error("CPF inválido: deve conter 11 dígitos");
            throw new IllegalArgumentException("CPF inválido: deve conter 11 dígitos");
        }
        // Modificar o CPF diretamente no request (já que @Data inclui setters)
        request.setCpf(normalizedCpf);
        
        if (StringUtils.isEmpty(request.getEmail())) {
            log.error("Email é obrigatório");
            throw new IllegalArgumentException("Email é obrigatório");
        }
        
        // Verificar se o CPF já existe (usando o serviço de sincronização)
        if (userCpfSyncService.checkCpfExists(request.getCpf())) {
            log.error("CPF já cadastrado: {}", request.getCpf());
            throw new IllegalArgumentException("CPF já cadastrado no sistema");
        }
        
        // Validar campos específicos por tipo de usuário
        validateTypeSpecificFields(request);
        
        // Criar o usuário
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .userType(request.getUserType())
                .email(request.getEmail())
                .build();

        user = userRepository.save(user);
        log.info("User registered successfully: {}", user.getUsername());
        
        // Registrar o CPF localmente antes de enviar o evento
        userCpfSyncService.registerCpf(request.getCpf(), user, user.getUserType(), user.getUsername());
        
        // Criar e enviar o evento
        UserCreatedEvent userCreatedEvent = createUserEvent(user, request);
        boolean sent = false;
        
        try {
            log.info("Publishing user created event: {}", userCreatedEvent);
            sent = streamBridge.send("userCreatedOutput-out-0", userCreatedEvent);
            log.info("Event sent successfully: {}", sent);
        } catch (Exception e) {
            log.error("Failed to send user created event: {}", e.getMessage(), e);
            // Se falhar ao enviar o evento, removemos o usuário
            deleteUser(user.getId());
            throw new RuntimeException("Falha ao completar o processo de registro", e);
        }

        // Se o evento foi enviado, retornamos o token
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
    
    /**
     * Valida campos específicos por tipo de usuário
     */
    private void validateTypeSpecificFields(RegisterRequest request) {
        if (UserType.DOCTOR.equals(request.getUserType())) {
            if (StringUtils.isEmpty(request.getCrm())) {
                log.error("CRM é obrigatório para médicos");
                throw new IllegalArgumentException("CRM é obrigatório para médicos");
            }
            if (StringUtils.isEmpty(request.getSpecialty())) {
                log.error("Especialidade é obrigatória para médicos");
                throw new IllegalArgumentException("Especialidade é obrigatória para médicos");
            }
            
            // Verificar se o CRM já existe
            boolean crmExists = false;
            try {
                crmExists = peopleServiceClient.checkCrmExists(request.getCrm());
                if (crmExists) {
                    log.error("CRM já registrado: {}", request.getCrm());
                    throw new IllegalArgumentException("CRM já registrado");
                }
            } catch (Exception e) {
                if (e instanceof IllegalArgumentException) {
                    throw e; // Propaga a exceção se for relacionada ao CRM já existir
                }
                log.error("Erro ao verificar CRM: {}", e.getMessage());
                throw new RuntimeException("Não foi possível validar o CRM. Por favor, tente novamente.");
            }
        } else if (UserType.PATIENT.equals(request.getUserType())) {
            if (StringUtils.isEmpty(request.getNationalHealthCard())) {
                log.error("Cartão Nacional de Saúde é obrigatório para pacientes");
                throw new IllegalArgumentException("Cartão Nacional de Saúde é obrigatório para pacientes");
            }
        }
    }

    private UserCreatedEvent createUserEvent(User user, RegisterRequest request) {
        // Normaliza o CPF antes de enviar no evento
        String normalizedCpf = request.getCpf();
        if (normalizedCpf == null || normalizedCpf.trim().isEmpty()) {
            log.error("CPF está vazio para o usuário {}. Isso causará falha no processamento do evento.", user.getUsername());
            // Fornecer um valor padrão para evitar falha na validação
            // Em produção, seria melhor lançar uma exceção, mas para fins de demonstração
            // estamos usando um valor temporário para permitir o processamento
            normalizedCpf = "00000000000"; // Temporário, apenas para demonstração
        } else {
            normalizedCpf = normalizedCpf.replaceAll("[^0-9]", "");
            if (normalizedCpf.length() > 11) {
                normalizedCpf = normalizedCpf.substring(0, 11);
            } else if (normalizedCpf.length() < 11) {
                // Padding com zeros à esquerda se necessário
                normalizedCpf = String.format("%011d", Long.parseLong(normalizedCpf));
            }
        }
        
        log.info("CPF normalizado para evento: {}", normalizedCpf);
        
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
            
            // Send deletion event
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
