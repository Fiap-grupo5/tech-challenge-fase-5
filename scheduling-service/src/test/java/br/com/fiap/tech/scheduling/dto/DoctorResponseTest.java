package br.com.fiap.tech.scheduling.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DoctorResponseTest {

    @Test
    void deveDefinirEObterCampos() {
        DoctorResponse doctor = new DoctorResponse();
        LocalDateTime agora = LocalDateTime.now();

        doctor.setId(1L);
        doctor.setName("Dr. John Doe");
        doctor.setEmail("johndoe@example.com");
        doctor.setCrm("123456");
        doctor.setSpecialty("Cardiology");
        doctor.setPhoneNumber("123-456-7890");
        doctor.setCreatedAt(agora);
        doctor.setUpdatedAt(agora);

        assertThat(doctor.getId()).isEqualTo(1L);
        assertThat(doctor.getName()).isEqualTo("Dr. John Doe");
        assertThat(doctor.getEmail()).isEqualTo("johndoe@example.com");
        assertThat(doctor.getCrm()).isEqualTo("123456");
        assertThat(doctor.getSpecialty()).isEqualTo("Cardiology");
        assertThat(doctor.getPhoneNumber()).isEqualTo("123-456-7890");
        assertThat(doctor.getCreatedAt()).isEqualTo(agora);
        assertThat(doctor.getUpdatedAt()).isEqualTo(agora);
    }

    @Test
    void deveVerificarEqualsEHashCode() {
        LocalDateTime agora = LocalDateTime.now();

        DoctorResponse doctor1 = new DoctorResponse();
        doctor1.setId(1L);
        doctor1.setName("Dr. John Doe");
        doctor1.setEmail("johndoe@example.com");
        doctor1.setCrm("123456");
        doctor1.setSpecialty("Cardiology");
        doctor1.setPhoneNumber("123-456-7890");
        doctor1.setCreatedAt(agora);
        doctor1.setUpdatedAt(agora);

        DoctorResponse doctor2 = new DoctorResponse();
        doctor2.setId(1L);
        doctor2.setName("Dr. John Doe");
        doctor2.setEmail("johndoe@example.com");
        doctor2.setCrm("123456");
        doctor2.setSpecialty("Cardiology");
        doctor2.setPhoneNumber("123-456-7890");
        doctor2.setCreatedAt(agora);
        doctor2.setUpdatedAt(agora);

        assertThat(doctor1).isEqualTo(doctor2);
        assertThat(doctor1.hashCode()).hasSameHashCodeAs(doctor2.hashCode());
    }

    @Test
    void deveVerificarToString() {
        LocalDateTime agora = LocalDateTime.now();

        DoctorResponse doctor = new DoctorResponse();
        doctor.setId(1L);
        doctor.setName("Dr. John Doe");
        doctor.setEmail("johndoe@example.com");
        doctor.setCrm("123456");
        doctor.setSpecialty("Cardiology");
        doctor.setPhoneNumber("123-456-7890");
        doctor.setCreatedAt(agora);
        doctor.setUpdatedAt(agora);

        String toString = doctor.toString();
        assertThat(toString).contains("1", "Dr. John Doe", "johndoe@example.com", "123456", "Cardiology", "123-456-7890", agora.toString());
    }
}