package br.com.fiap.tech.scheduling.dto;

import br.com.fiap.tech.scheduling.domain.Appointment;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppointmentWithPriorityDTOTest {

    @Test
    void shouldCreateDTOWithAllArgsConstructor() {
        Appointment appointment = new Appointment();
        Integer priorityScore = 80;
        Integer estimatedWaitingDays = 3;

        AppointmentWithPriorityDTO dto = new AppointmentWithPriorityDTO(appointment, priorityScore, estimatedWaitingDays);

        assertThat(dto.getAppointment()).isEqualTo(appointment);
        assertThat(dto.getPriorityScore()).isEqualTo(priorityScore);
        assertThat(dto.getEstimatedWaitingDays()).isEqualTo(estimatedWaitingDays);
    }

    @Test
    void shouldCreateDTOWithPriorityBasedConstructor() {
        Appointment appointment = new Appointment();
        Integer priorityScore = 85;

        AppointmentWithPriorityDTO dto = new AppointmentWithPriorityDTO(appointment, priorityScore);

        assertThat(dto.getAppointment()).isEqualTo(appointment);
        assertThat(dto.getPriorityScore()).isEqualTo(priorityScore);
        assertThat(dto.getEstimatedWaitingDays()).isEqualTo(3); // Alta prioridade
    }

    @Test
    void shouldSetEstimatedWaitingDaysBasedOnPriorityScore() {
        Appointment appointment = new Appointment();

        AppointmentWithPriorityDTO urgent = new AppointmentWithPriorityDTO(appointment, 100);
        AppointmentWithPriorityDTO high = new AppointmentWithPriorityDTO(appointment, 70);
        AppointmentWithPriorityDTO medium = new AppointmentWithPriorityDTO(appointment, 50);
        AppointmentWithPriorityDTO low = new AppointmentWithPriorityDTO(appointment, 20);

        assertThat(urgent.getEstimatedWaitingDays()).isEqualTo(1); // Muito urgente
        assertThat(high.getEstimatedWaitingDays()).isEqualTo(3); // Alta prioridade
        assertThat(medium.getEstimatedWaitingDays()).isEqualTo(7); // Média prioridade
        assertThat(low.getEstimatedWaitingDays()).isEqualTo(14); // Baixa prioridade
    }

    @Test
    void shouldVerifyEqualsAndHashCode() {
        Appointment appointment = new Appointment();
        AppointmentWithPriorityDTO dto1 = new AppointmentWithPriorityDTO(appointment, 80, 3);
        AppointmentWithPriorityDTO dto2 = new AppointmentWithPriorityDTO(appointment, 80, 3);

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void shouldVerifyToString() {
        Appointment appointment = new Appointment();
        AppointmentWithPriorityDTO dto = new AppointmentWithPriorityDTO(appointment, 80, 3);

        String toString = dto.toString();
        assertThat(toString).contains("appointment", "80", "3");
    }
}