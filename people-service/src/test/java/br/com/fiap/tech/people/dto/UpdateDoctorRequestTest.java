package br.com.fiap.tech.people.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateDoctorRequestTest {

    @Test
    void shouldSetAndGetId() {
        UpdateDoctorRequest request = new UpdateDoctorRequest();
        request.setId(1L);

        assertThat(request.getId()).isEqualTo(1L);
    }

    @Test
    void shouldSetAndGetFullName() {
        UpdateDoctorRequest request = new UpdateDoctorRequest();
        request.setFullName("Gabigol");

        assertThat(request.getFullName()).isEqualTo("Gabigol");
    }

    @Test
    void shouldSetAndGetCpf() {
        UpdateDoctorRequest request = new UpdateDoctorRequest();
        request.setCpf("12345678900");

        assertThat(request.getCpf()).isEqualTo("12345678900");
    }

    @Test
    void shouldSetAndGetCrm() {
        UpdateDoctorRequest request = new UpdateDoctorRequest();
        request.setCrm("CRM12345");

        assertThat(request.getCrm()).isEqualTo("CRM12345");
    }

    @Test
    void shouldSetAndGetSpecialty() {
        UpdateDoctorRequest request = new UpdateDoctorRequest();
        request.setSpecialty("Cardiology");

        assertThat(request.getSpecialty()).isEqualTo("Cardiology");
    }

    @Test
    void shouldSetAndGetEmail() {
        UpdateDoctorRequest request = new UpdateDoctorRequest();
        request.setEmail("tyrion.lannister@example.com");

        assertThat(request.getEmail()).isEqualTo("tyrion.lannister@example.com");
    }

    @Test
    void shouldSetAndGetPhoneNumber() {
        UpdateDoctorRequest request = new UpdateDoctorRequest();
        request.setPhoneNumber("987654321");

        assertThat(request.getPhoneNumber()).isEqualTo("987654321");
    }

    @Test
    void shouldVerifyEqualsAndHashCode() {
        UpdateDoctorRequest request1 = UpdateDoctorRequest.builder()
                .id(1L)
                .fullName("Eren Yeager")
                .cpf("12345678900")
                .crm("CRM12345")
                .specialty("Orthopedics")
                .email("eren.yeager@example.com")
                .phoneNumber("987654321")
                .build();

        UpdateDoctorRequest request2 = UpdateDoctorRequest.builder()
                .id(1L)
                .fullName("Eren Yeager")
                .cpf("12345678900")
                .crm("CRM12345")
                .specialty("Orthopedics")
                .email("eren.yeager@example.com")
                .phoneNumber("987654321")
                .build();

        assertThat(request1)
                .isEqualTo(request2)
                .hasSameHashCodeAs(request2);
    }
}