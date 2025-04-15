package br.com.fiap.tech.identity.service;

import br.com.fiap.tech.identity.domain.User;
import br.com.fiap.tech.identity.domain.UserType;
import br.com.fiap.tech.identity.dto.RegisterRequest;
import br.com.fiap.tech.identity.events.UserCreatedEvent;
import br.com.fiap.tech.identity.repository.UserRepository;
import br.com.fiap.tech.identity.security.JwtService;
import br.com.fiap.tech.identity.service.client.PeopleServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private StreamBridge streamBridge;
    
    @Mock
    private PeopleServiceClient peopleServiceClient;
    
    @Mock
    private PeopleService peopleService;

    @Mock
    private UserCpfSyncService userCpfSyncService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<UserCreatedEvent> eventCaptor;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(
                userRepository,
                passwordEncoder,
                jwtService,
                authenticationManager,
                streamBridge,
                peopleServiceClient,
                peopleService,
                userCpfSyncService
        );

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt_token");
        when(streamBridge.send(anyString(), any())).thenReturn(true);
        when(peopleServiceClient.checkCpfExists(anyString())).thenReturn(false);
        when(peopleServiceClient.checkCrmExists(anyString())).thenReturn(false);
        when(peopleService.checkCpfExists(anyString())).thenReturn(false);
        when(peopleService.checkCrmExists(anyString())).thenReturn(false);
        when(userCpfSyncService.checkCpfExists(anyString())).thenReturn(false);
    }

    @Test
    @DisplayName("Should register a patient successfully")
    void shouldRegisterPatientSuccessfully() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .username("patient@example.com")
                .password("password")
                .userType(UserType.PATIENT)
                .fullName("João Silva")
                .cpf("12345678900")
                .email("patient@example.com")
                .nationalHealthCard("123456789")
                .phoneNumber("11999999999")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Rua Exemplo, 123")
                .city("São Paulo")
                .state("SP")
                .zipCode("01234567")
                .build();

        // When
        var response = authenticationService.register(request);

        // Then
        assertNotNull(response);
        assertEquals("jwt_token", response.getToken());

        verify(userRepository).save(userCaptor.capture());
        assertEquals(UserType.PATIENT, userCaptor.getValue().getUserType());

        verify(streamBridge).send(eq("userCreatedOutput-out-0"), eventCaptor.capture());
        UserCreatedEvent event = eventCaptor.getValue();
        assertEquals("PATIENT", event.getUserType());
        assertEquals("João Silva", event.getFullName());
        assertEquals("12345678900", event.getCpf());
        assertEquals("patient@example.com", event.getEmail());
        assertEquals("123456789", event.getNationalHealthCard());
    }

    @Test
    @DisplayName("Should register a doctor successfully")
    void shouldRegisterDoctorSuccessfully() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .username("doctor@example.com")
                .password("password")
                .userType(UserType.DOCTOR)
                .fullName("Dr. Carlos Oliveira")
                .cpf("98765432100")
                .email("doctor@example.com")
                .crm("CRM/SP 12345")
                .specialty("Cardiologia")
                .phoneNumber("11988888888")
                .build();

        // When
        var response = authenticationService.register(request);

        // Then
        assertNotNull(response);
        assertEquals("jwt_token", response.getToken());

        verify(userRepository).save(userCaptor.capture());
        assertEquals(UserType.DOCTOR, userCaptor.getValue().getUserType());

        verify(streamBridge).send(eq("userCreatedOutput-out-0"), eventCaptor.capture());
        UserCreatedEvent event = eventCaptor.getValue();
        assertEquals("DOCTOR", event.getUserType());
        assertEquals("Dr. Carlos Oliveira", event.getFullName());
        assertEquals("98765432100", event.getCpf());
        assertEquals("doctor@example.com", event.getEmail());
        assertEquals("CRM/SP 12345", event.getCrm());
        assertEquals("Cardiologia", event.getSpecialty());
    }

    @Test
    @DisplayName("Should register an administrator successfully")
    void shouldRegisterAdministratorSuccessfully() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .username("admin@example.com")
                .password("password")
                .userType(UserType.ADMINISTRATOR)
                .fullName("Admin Silva")
                .cpf("11122233344")
                .email("admin@example.com")
                .phoneNumber("11977777777")
                .build();

        // When
        var response = authenticationService.register(request);

        // Then
        assertNotNull(response);
        assertEquals("jwt_token", response.getToken());

        verify(userRepository).save(userCaptor.capture());
        assertEquals(UserType.ADMINISTRATOR, userCaptor.getValue().getUserType());

        verify(streamBridge).send(eq("userCreatedOutput-out-0"), eventCaptor.capture());
        UserCreatedEvent event = eventCaptor.getValue();
        assertEquals("ADMINISTRATOR", event.getUserType());
        assertEquals("Admin Silva", event.getFullName());
        assertEquals("11122233344", event.getCpf());
        assertEquals("admin@example.com", event.getEmail());
    }

    @Test
    @DisplayName("Should fail when registering a patient without a National Health Card")
    void shouldFailWhenPatientWithoutNationalHealthCard() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .username("patient@example.com")
                .password("password")
                .userType(UserType.PATIENT)
                .fullName("João Silva")
                .cpf("12345678900")
                .email("patient@example.com")
                .build();

        // When - Then
        assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.register(request);
        });
    }

    @Test
    @DisplayName("Should fail when registering a doctor without a CRM")
    void shouldFailWhenDoctorWithoutCrm() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .username("doctor@example.com")
                .password("password")
                .userType(UserType.DOCTOR)
                .fullName("Dr. Carlos Oliveira")
                .cpf("98765432100")
                .email("doctor@example.com")
                .specialty("Cardiologia")
                .build();

        // When - Then
        assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.register(request);
        });
    }

    @Test
    @DisplayName("Should fail when registering a doctor without a specialty")
    void shouldFailWhenDoctorWithoutSpecialty() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .username("doctor@example.com")
                .password("password")
                .userType(UserType.DOCTOR)
                .fullName("Dr. Carlos Oliveira")
                .cpf("98765432100")
                .email("doctor@example.com")
                .crm("CRM/SP 12345")
                .build();

        // When - Then
        assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.register(request);
        });
    }

    @Test
    @DisplayName("Should fail when registering any user without an email")
    void shouldFailWhenUserWithoutEmail() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .username("user@example.com")
                .password("password")
                .userType(UserType.ADMINISTRATOR)
                .fullName("User Silva")
                .cpf("12345678900")
                .build();

        // When - Then
        assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.register(request);
        });
    }
} 