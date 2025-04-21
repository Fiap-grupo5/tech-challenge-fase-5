package br.com.fiap.tech.people.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PatientRequestTest {

    @Test
    void shouldSetAndGetFullName() {
        PatientRequest request = new PatientRequest();
        request.setFullName("Zico");

        assertThat(request.getFullName()).isEqualTo("Zico");
    }

    @Test
    void shouldSetAndGetCpf() {
        PatientRequest request = new PatientRequest();
        request.setCpf("12345678900");

        assertThat(request.getCpf()).isEqualTo("12345678900");
    }

    @Test
    void shouldSetAndGetNationalHealthCard() {
        PatientRequest request = new PatientRequest();
        request.setNationalHealthCard("SUS123456789");

        assertThat(request.getNationalHealthCard()).isEqualTo("SUS123456789");
    }

    @Test
    void shouldSetAndGetEmail() {
        PatientRequest request = new PatientRequest();
        request.setEmail("daenerys.targaryen@example.com");

        assertThat(request.getEmail()).isEqualTo("daenerys.targaryen@example.com");
    }

    @Test
    void shouldSetAndGetBirthDate() {
        PatientRequest request = new PatientRequest();
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        request.setBirthDate(birthDate);

        assertThat(request.getBirthDate()).isEqualTo(birthDate);
    }

    @Test
    void shouldSetAndGetPhoneNumber() {
        PatientRequest request = new PatientRequest();
        request.setPhoneNumber("987654321");

        assertThat(request.getPhoneNumber()).isEqualTo("987654321");
    }

    @Test
    void shouldSetAndGetAddress() {
        PatientRequest request = new PatientRequest();
        request.setAddress("Rua dos Alquimistas, 123");

        assertThat(request.getAddress()).isEqualTo("Rua dos Alquimistas, 123");
    }

    @Test
    void shouldSetAndGetCity() {
        PatientRequest request = new PatientRequest();
        request.setCity("Shiganshina");

        assertThat(request.getCity()).isEqualTo("Shiganshina");
    }

    @Test
    void shouldSetAndGetState() {
        PatientRequest request = new PatientRequest();
        request.setState("Paradis");

        assertThat(request.getState()).isEqualTo("Paradis");
    }

    @Test
    void shouldSetAndGetZipCode() {
        PatientRequest request = new PatientRequest();
        request.setZipCode("12345-678");

        assertThat(request.getZipCode()).isEqualTo("12345-678");
    }

    @Test
    void shouldVerifyEqualsAndHashCode() {
        PatientRequest request1 = new PatientRequest();
        request1.setFullName("Edward Elric");
        request1.setCpf("12345678900");
        request1.setNationalHealthCard("SUS123456789");
        request1.setEmail("edward.elric@example.com");
        request1.setBirthDate(LocalDate.of(1990, 1, 1));
        request1.setPhoneNumber("987654321");
        request1.setAddress("Rua dos Alquimistas, 123");
        request1.setCity("Central");
        request1.setState("Amestris");
        request1.setZipCode("12345-678");

        PatientRequest request2 = new PatientRequest();
        request2.setFullName("Edward Elric");
        request2.setCpf("12345678900");
        request2.setNationalHealthCard("SUS123456789");
        request2.setEmail("edward.elric@example.com");
        request2.setBirthDate(LocalDate.of(1990, 1, 1));
        request2.setPhoneNumber("987654321");
        request2.setAddress("Rua dos Alquimistas, 123");
        request2.setCity("Central");
        request2.setState("Amestris");
        request2.setZipCode("12345-678");

        assertThat(request1)
                .isEqualTo(request2)
                .hasSameHashCodeAs(request2);
    }
}