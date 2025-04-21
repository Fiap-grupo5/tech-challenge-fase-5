package br.com.fiap.tech.identity.dto;

import br.com.fiap.tech.identity.domain.UserType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterRequestTest {

    @Test
    void shouldCreateRegisterRequestWithBuilder() {
        RegisterRequest request = RegisterRequest.builder()
                .username("joao123")
                .password("senha123")
                .userType(UserType.PATIENT)
                .fullName("João Silva")
                .cpf("123.456.789-00")
                .email("joao.silva@example.com")
                .nationalHealthCard("123456789")
                .phoneNumber("11999999999")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Rua Exemplo, 123")
                .city("São Paulo")
                .state("SP")
                .zipCode("12345-678")
                .build();

        assertThat(request)
                .extracting(
                        RegisterRequest::getUsername,
                        RegisterRequest::getPassword,
                        RegisterRequest::getUserType,
                        RegisterRequest::getFullName,
                        RegisterRequest::getCpf,
                        RegisterRequest::getEmail,
                        RegisterRequest::getNationalHealthCard,
                        RegisterRequest::getPhoneNumber,
                        RegisterRequest::getBirthDate,
                        RegisterRequest::getAddress,
                        RegisterRequest::getCity,
                        RegisterRequest::getState,
                        RegisterRequest::getZipCode
                )
                .containsExactly(
                        "joao123",
                        "senha123",
                        UserType.PATIENT,
                        "João Silva",
                        "123.456.789-00",
                        "joao.silva@example.com",
                        "123456789",
                        "11999999999",
                        LocalDate.of(1990, 1, 1),
                        "Rua Exemplo, 123",
                        "São Paulo",
                        "SP",
                        "12345-678"
                );
    }
}