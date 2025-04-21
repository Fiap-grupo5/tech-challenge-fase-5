package br.com.fiap.tech.people.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UpdatePatientRequestTest {

    @Test
    void shouldSetAndGetId() {
        UpdatePatientRequest request = new UpdatePatientRequest();
        request.setId(1L);

        assertThat(request.getId()).isEqualTo(1L);
    }

    @Test
    void shouldSetAndGetFullName() {
        UpdatePatientRequest request = new UpdatePatientRequest();
        request.setFullName("Zico");

        assertThat(request.getFullName()).isEqualTo("Zico");
    }

    @Test
    void shouldSetAndGetCpf() {
        UpdatePatientRequest request = new UpdatePatientRequest();
        request.setCpf("12345678900");

        assertThat(request.getCpf()).isEqualTo("12345678900");
    }

    @Test
    void shouldSetAndGetNationalHealthCard() {
        UpdatePatientRequest request = new UpdatePatientRequest();
        request.setNationalHealthCard("SUS123456789");

        assertThat(request.getNationalHealthCard()).isEqualTo("SUS123456789");
    }

    @Test
    void shouldSetAndGetEmail() {
        UpdatePatientRequest request = new UpdatePatientRequest();
        request.setEmail("daenerys.targaryen@example.com");

        assertThat(request.getEmail()).isEqualTo("daenerys.targaryen@example.com");
    }

    @Test
    void shouldSetAndGetBirthDate() {
        UpdatePatientRequest request = new UpdatePatientRequest();
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        request.setBirthDate(birthDate);

        assertThat(request.getBirthDate()).isEqualTo(birthDate);
    }

    @Test
    void shouldSetAndGetPhoneNumber() {
        UpdatePatientRequest request = new UpdatePatientRequest();
        request.setPhoneNumber("987654321");

        assertThat(request.getPhoneNumber()).isEqualTo("987654321");
    }

    @Test
    void shouldSetAndGetAddress() {
        UpdatePatientRequest request = new UpdatePatientRequest();
        request.setAddress("Rua dos Alquimistas, 123");

        assertThat(request.getAddress()).isEqualTo("Rua dos Alquimistas, 123");
    }

    @Test
    void shouldSetAndGetCity() {
        UpdatePatientRequest request = new UpdatePatientRequest();
        request.setCity("Shiganshina");

        assertThat(request.getCity()).isEqualTo("Shiganshina");
    }

    @Test
    void shouldSetAndGetState() {
        UpdatePatientRequest request = new UpdatePatientRequest();
        request.setState("Paradis");

        assertThat(request.getState()).isEqualTo("Paradis");
    }

    @Test
    void shouldSetAndGetZipCode() {
        UpdatePatientRequest request = new UpdatePatientRequest();
        request.setZipCode("12345-678");

        assertThat(request.getZipCode()).isEqualTo("12345-678");
    }

    @Test
    void shouldVerifyEqualsAndHashCode() {
        UpdatePatientRequest request1 = UpdatePatientRequest.builder()
                .id(1L)
                .fullName("Edward Elric")
                .cpf("12345678900")
                .nationalHealthCard("SUS123456789")
                .email("edward.elric@example.com")
                .birthDate(LocalDate.of(1990, 1, 1))
                .phoneNumber("987654321")
                .address("Rua dos Alquimistas, 123")
                .city("Central")
                .state("Amestris")
                .zipCode("12345-678")
                .build();

        UpdatePatientRequest request2 = UpdatePatientRequest.builder()
                .id(1L)
                .fullName("Edward Elric")
                .cpf("12345678900")
                .nationalHealthCard("SUS123456789")
                .email("edward.elric@example.com")
                .birthDate(LocalDate.of(1990, 1, 1))
                .phoneNumber("987654321")
                .address("Rua dos Alquimistas, 123")
                .city("Central")
                .state("Amestris")
                .zipCode("12345-678")
                .build();

        assertThat(request1)
                .isEqualTo(request2)
                .hasSameHashCodeAs(request2);
    }
}