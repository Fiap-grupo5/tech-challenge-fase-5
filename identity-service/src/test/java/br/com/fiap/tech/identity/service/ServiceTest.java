package br.com.fiap.tech.identity.service;

import br.com.fiap.tech.identity.domain.User;
import br.com.fiap.tech.identity.domain.UserType;
import br.com.fiap.tech.identity.dto.AuthenticationRequest;
import br.com.fiap.tech.identity.dto.AuthenticationResponse;
import br.com.fiap.tech.identity.dto.RegisterRequest;
import br.com.fiap.tech.identity.repository.UserRepository;
import br.com.fiap.tech.identity.security.JwtService;
import br.com.fiap.tech.identity.service.client.PeopleServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private PeopleServiceClient peopleServiceClient;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserCpfSyncService userCpfSyncService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private StreamBridge streamBridge;

    @Mock
    private PeopleService peopleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(streamBridge.send(anyString(), any())).thenReturn(true);
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("levi");
        request.setPassword("password");
        request.setCpf("12345678900");
        request.setEmail("levi@example.com");
        request.setFullName("Levi Ackerman");
        request.setUserType(UserType.PATIENT);
        request.setNationalHealthCard("123456789");

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userCpfSyncService.checkCpfExists(request.getCpf())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthenticationResponse response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals("levi", response.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUsernameExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existinguser");

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.register(request);
        });

        assertEquals("Username already exists.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAuthenticateSuccess() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("levi");
        request.setPassword("password");

        User user = new User();
        user.setId(1L);
        user.setUsername("levi");

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals(1L, response.getUserId());
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    void testCheckAdministratorExistsTrue() {
        User admin = new User();
        admin.setId(1L);
        admin.setUserType(UserType.ADMINISTRATOR);

        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

        boolean exists = authenticationService.checkAdministratorExists(1L);

        assertTrue(exists);
    }

    @Test
    void testCheckAdministratorExistsFalse() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        boolean exists = authenticationService.checkAdministratorExists(1L);

        assertFalse(exists);
    }

    @Test
    void testRegisterDoctorMissingCrm() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("Itadori");
        request.setPassword("password");
        request.setCpf("12345678900");
        request.setEmail("itadori@example.com");
        request.setFullName("Doctor Itadori");
        request.setUserType(UserType.DOCTOR);
        request.setSpecialty("Cardiology");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.register(request);
        });

        assertEquals("CRM is mandatory for doctors.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterDoctorSuccess() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("Zeke");
        request.setPassword("password");
        request.setCpf("12345678900");
        request.setEmail("doctor.zeke@example.com");
        request.setFullName("Doctor Zeke");
        request.setUserType(UserType.DOCTOR);
        request.setCrm("CRM12345");
        request.setSpecialty("Neurology");

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userCpfSyncService.checkCpfExists(request.getCpf())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(peopleServiceClient.checkCrmExists(request.getCrm())).thenReturn(false);

        AuthenticationResponse response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals("Zeke", response.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterAdministratorSuccess() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("Eren");
        request.setPassword("admpassword");
        request.setCpf("98765432100");
        request.setEmail("eren@example.com");
        request.setFullName("Eren Yeager");
        request.setUserType(UserType.ADMINISTRATOR);

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userCpfSyncService.checkCpfExists(request.getCpf())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthenticationResponse response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals("Eren", response.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testAuthenticateInvalidCredentials() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("levi");
        request.setPassword("wrongpassword");

        doThrow(new RuntimeException("Invalid credentials")).when(authenticationManager).authenticate(any());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationService.authenticate(request);
        });

        assertEquals("Invalid credentials", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    void testRegisterCpfAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("levi");
        request.setCpf("12345678900");
        request.setFullName("Levi Ackerman");
        request.setEmail("levi@example.com");
        request.setUserType(UserType.PATIENT);
        request.setNationalHealthCard("123456789");

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userCpfSyncService.checkCpfExists(request.getCpf())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.register(request);
        });

        assertEquals("CPF already registered in system", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCheckCpfExists_NullOrEmpty() {
        assertFalse(peopleService.checkCpfExists(null));
        assertFalse(peopleService.checkCpfExists(""));
        assertFalse(peopleService.checkCpfExists("   "));
    }

    @Test
    void testCheckCrmExists_NullOrEmpty() {
        assertFalse(peopleService.checkCrmExists(null));
        assertFalse(peopleService.checkCrmExists(""));
        assertFalse(peopleService.checkCrmExists("   "));
    }

}