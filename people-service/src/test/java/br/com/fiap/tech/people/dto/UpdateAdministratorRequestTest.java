package br.com.fiap.tech.people.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateAdministratorRequestTest {

    @Test
    void shouldSetAndGetId() {
        UpdateAdministratorRequest request = new UpdateAdministratorRequest();
        request.setId(1L);

        assertThat(request.getId()).isEqualTo(1L);
    }

    @Test
    void shouldSetAndGetFullName() {
        UpdateAdministratorRequest request = new UpdateAdministratorRequest();
        request.setFullName("Admin Name");

        assertThat(request.getFullName()).isEqualTo("Admin Name");
    }

    @Test
    void shouldSetAndGetCpf() {
        UpdateAdministratorRequest request = new UpdateAdministratorRequest();
        request.setCpf("12345678900");

        assertThat(request.getCpf()).isEqualTo("12345678900");
    }

    @Test
    void shouldSetAndGetEmail() {
        UpdateAdministratorRequest request = new UpdateAdministratorRequest();
        request.setEmail("admin@example.com");

        assertThat(request.getEmail()).isEqualTo("admin@example.com");
    }

    @Test
    void shouldSetAndGetPhoneNumber() {
        UpdateAdministratorRequest request = new UpdateAdministratorRequest();
        request.setPhoneNumber("987654321");

        assertThat(request.getPhoneNumber()).isEqualTo("987654321");
    }

    @Test
    void shouldVerifyEqualsAndHashCode() {
        UpdateAdministratorRequest request1 = UpdateAdministratorRequest.builder()
                .id(1L)
                .fullName("Admin Name")
                .cpf("12345678900")
                .email("admin@example.com")
                .phoneNumber("987654321")
                .build();

        UpdateAdministratorRequest request2 = UpdateAdministratorRequest.builder()
                .id(1L)
                .fullName("Admin Name")
                .cpf("12345678900")
                .email("admin@example.com")
                .phoneNumber("987654321")
                .build();

        assertThat(request1)
                .isEqualTo(request2)
                .hasSameHashCodeAs(request2);
    }
}