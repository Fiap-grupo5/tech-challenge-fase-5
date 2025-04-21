package br.com.fiap.tech.scheduling.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PatientResponseTest {

    private PatientResponse createPatient(Long id, String name, String email, String cpf, String address, String insuranceNumber) {
        LocalDate birthDate = LocalDate.of(1980, 5, 5);
        LocalDateTime createdAt = LocalDateTime.of(2023, 10, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2023, 10, 2, 15, 30);

        PatientResponse patient = new PatientResponse();
        patient.setId(id);
        patient.setName(name);
        patient.setEmail(email);
        patient.setCpf(cpf);
        patient.setBirthDate(birthDate);
        patient.setPhoneNumber("(11) 98765-4321");
        patient.setAddress(address);
        patient.setInsuranceProvider("HealthCare Inc.");
        patient.setInsuranceNumber(insuranceNumber);
        patient.setCreatedAt(createdAt);
        patient.setUpdatedAt(updatedAt);

        return patient;
    }

    @Test
    void shouldSetAndGetFields() {
        PatientResponse patient = createPatient(1L, "Jax Teller", "jax.teller@example.com", "123.456.789-00", "123 Main St", "HC123456");

        assertThat(patient.getId()).isEqualTo(1L);
        assertThat(patient.getName()).isEqualTo("Jax Teller");
        assertThat(patient.getEmail()).isEqualTo("jax.teller@example.com");
        assertThat(patient.getCpf()).isEqualTo("123.456.789-00");
        assertThat(patient.getBirthDate()).isEqualTo(LocalDate.of(1980, 5, 5));
        assertThat(patient.getPhoneNumber()).isEqualTo("(11) 98765-4321");
        assertThat(patient.getAddress()).isEqualTo("123 Main St");
        assertThat(patient.getInsuranceProvider()).isEqualTo("HealthCare Inc.");
        assertThat(patient.getInsuranceNumber()).isEqualTo("HC123456");
        assertThat(patient.getCreatedAt()).isEqualTo(LocalDateTime.of(2023, 10, 1, 10, 0));
        assertThat(patient.getUpdatedAt()).isEqualTo(LocalDateTime.of(2023, 10, 2, 15, 30));
    }

    @Test
    void shouldVerifyEqualsAndHashCode() {
        PatientResponse patient1 = createPatient(1L, "Gemma Teller", "gemma.teller@example.com", "987.654.321-00", "456 Main St", "HC654321");
        PatientResponse patient2 = createPatient(1L, "Gemma Teller", "gemma.teller@example.com", "987.654.321-00", "456 Main St", "HC654321");

        assertThat(patient1)
                .isEqualTo(patient2)
                .hasSameHashCodeAs(patient2);
    }

    @Test
    void shouldVerifyToString() {
        PatientResponse patient = createPatient(1L, "Opie Winston", "opie.winston@example.com", "456.789.123-00", "789 Main St", "HC789123");

        String toString = patient.toString();
        assertThat(toString).contains("1", "Opie Winston", "opie.winston@example.com", "456.789.123-00", "789 Main St", "HC789123");
    }
}