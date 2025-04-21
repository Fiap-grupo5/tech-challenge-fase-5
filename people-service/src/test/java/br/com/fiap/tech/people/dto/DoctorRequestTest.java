package br.com.fiap.tech.people.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DoctorRequestTest {

    @Test
    void shouldSetAndGetFullName() {
        DoctorRequest request = new DoctorRequest();
        request.setFullName("Gabigol");

        assertThat(request.getFullName()).isEqualTo("Gabigol");
    }

    @Test
    void shouldSetAndGetCpf() {
        DoctorRequest request = new DoctorRequest();
        request.setCpf("12345678900");

        assertThat(request.getCpf()).isEqualTo("12345678900");
    }

    @Test
    void shouldSetAndGetCrm() {
        DoctorRequest request = new DoctorRequest();
        request.setCrm("CRM12345");

        assertThat(request.getCrm()).isEqualTo("CRM12345");
    }

    @Test
    void shouldSetAndGetSpecialty() {
        DoctorRequest request = new DoctorRequest();
        request.setSpecialty("Cardiology");

        assertThat(request.getSpecialty()).isEqualTo("Cardiology");
    }

    @Test
    void shouldSetAndGetEmail() {
        DoctorRequest request = new DoctorRequest();
        request.setEmail("tyrion.lannister@example.com");

        assertThat(request.getEmail()).isEqualTo("tyrion.lannister@example.com");
    }

    @Test
    void shouldSetAndGetPhoneNumber() {
        DoctorRequest request = new DoctorRequest();
        request.setPhoneNumber("123456789");

        assertThat(request.getPhoneNumber()).isEqualTo("123456789");
    }

    @Test
    void shouldVerifyEqualsAndHashCode() {
        DoctorRequest request1 = new DoctorRequest();
        request1.setFullName("Eren Yeager");
        request1.setCpf("12345678900");
        request1.setCrm("CRM12345");
        request1.setSpecialty("Orthopedics");
        request1.setEmail("eren.yeager@example.com");
        request1.setPhoneNumber("987654321");

        DoctorRequest request2 = new DoctorRequest();
        request2.setFullName("Eren Yeager");
        request2.setCpf("12345678900");
        request2.setCrm("CRM12345");
        request2.setSpecialty("Orthopedics");
        request2.setEmail("eren.yeager@example.com");
        request2.setPhoneNumber("987654321");

        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }
}