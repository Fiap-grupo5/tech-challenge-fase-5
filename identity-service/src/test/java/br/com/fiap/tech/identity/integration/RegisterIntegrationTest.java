package br.com.fiap.tech.identity.integration;

import br.com.fiap.tech.identity.domain.UserType;
import br.com.fiap.tech.identity.dto.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RegisterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private StreamBridge streamBridge;
    
    @BeforeEach
    void setUp() {
        when(streamBridge.send(anyString(), any())).thenReturn(true);
    }

    @Test
    @DisplayName("Deve registrar um paciente com sucesso")
    void shouldRegisterPatientSuccessfully() throws Exception {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .username("patient_test@example.com")
                .password("password123")
                .userType(UserType.PATIENT)
                .fullName("Paciente Teste")
                .cpf("12345678900")
                .email("patient_test@example.com")
                .nationalHealthCard("123456789")
                .phoneNumber("11999999999")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Rua Exemplo, 123")
                .city("São Paulo")
                .state("SP")
                .zipCode("01234567")
                .build();

        // When - Then
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("Deve registrar um médico com sucesso")
    void shouldRegisterDoctorSuccessfully() throws Exception {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .username("doctor_test@example.com")
                .password("password123")
                .userType(UserType.DOCTOR)
                .fullName("Dr. Teste")
                .cpf("98765432100")
                .email("doctor_test@example.com")
                .crm("CRM/SP 12345")
                .specialty("Cardiologia")
                .phoneNumber("11988888888")
                .build();

        // When - Then
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("Deve registrar um administrador com sucesso")
    void shouldRegisterAdministratorSuccessfully() throws Exception {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .username("admin_test@example.com")
                .password("password123")
                .userType(UserType.ADMINISTRATOR)
                .fullName("Admin Teste")
                .cpf("11122233344")
                .email("admin_test@example.com")
                .phoneNumber("11977777777")
                .build();

        // When - Then
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("Deve falhar ao registrar um médico sem CRM")
    void shouldFailToRegisterDoctorWithoutCrm() throws Exception {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .username("doctor_fail@example.com")
                .password("password123")
                .userType(UserType.DOCTOR)
                .fullName("Dr. Teste Falha")
                .cpf("98765432101")
                .email("doctor_fail@example.com")
                .specialty("Cardiologia")
                .phoneNumber("11988888889")
                .build();

        // When - Then
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve falhar ao registrar um paciente sem cartão SUS")
    void shouldFailToRegisterPatientWithoutNationalHealthCard() throws Exception {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .username("patient_fail@example.com")
                .password("password123")
                .userType(UserType.PATIENT)
                .fullName("Paciente Teste Falha")
                .cpf("12345678901")
                .email("patient_fail@example.com")
                .phoneNumber("11999999998")
                .build();

        // When - Then
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve falhar ao registrar qualquer usuário sem e-mail")
    void shouldFailToRegisterUserWithoutEmail() throws Exception {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .username("user_fail@example.com")
                .password("password123")
                .userType(UserType.ADMINISTRATOR)
                .fullName("User Teste Falha")
                .cpf("12345678902")
                .phoneNumber("11999999997")
                .build();

        // When - Then
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
} 