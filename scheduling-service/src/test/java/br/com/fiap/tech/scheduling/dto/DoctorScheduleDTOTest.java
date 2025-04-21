package br.com.fiap.tech.scheduling.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class DoctorScheduleDTOTest {

    @Test
    void deveDefinirEObterCampos() {
        LocalDateTime createdAt = LocalDateTime.of(2025, 5, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 5, 2, 15, 30);
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(12, 0);
        LocalTime secondPeriodStart = LocalTime.of(14, 0);
        LocalTime secondPeriodEnd = LocalTime.of(18, 0);

        DoctorScheduleDTO schedule = new DoctorScheduleDTO();
        schedule.setId(1L);
        schedule.setDayOfWeek("MONDAY");
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setSecondPeriodStart(secondPeriodStart);
        schedule.setSecondPeriodEnd(secondPeriodEnd);
        schedule.setDoctorId(101L);
        schedule.setFacilityId(202L);
        schedule.setCreatedAt(createdAt);
        schedule.setUpdatedAt(updatedAt);

        assertThat(schedule.getId()).isEqualTo(1L);
        assertThat(schedule.getDayOfWeek()).isEqualTo("MONDAY");
        assertThat(schedule.getStartTime()).isEqualTo(startTime);
        assertThat(schedule.getEndTime()).isEqualTo(endTime);
        assertThat(schedule.getSecondPeriodStart()).isEqualTo(secondPeriodStart);
        assertThat(schedule.getSecondPeriodEnd()).isEqualTo(secondPeriodEnd);
        assertThat(schedule.getDoctorId()).isEqualTo(101L);
        assertThat(schedule.getFacilityId()).isEqualTo(202L);
        assertThat(schedule.getCreatedAt()).isEqualTo(createdAt);
        assertThat(schedule.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void deveVerificarEqualsEHashCode() {
        LocalDateTime createdAt = LocalDateTime.of(2025, 5, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 5, 2, 15, 30);

        DoctorScheduleDTO schedule1 = new DoctorScheduleDTO(1L, "MONDAY", LocalTime.of(8, 0), LocalTime.of(12, 0),
                LocalTime.of(14, 0), LocalTime.of(18, 0), 101L, 202L, createdAt, updatedAt);

        DoctorScheduleDTO schedule2 = new DoctorScheduleDTO(1L, "MONDAY", LocalTime.of(8, 0), LocalTime.of(12, 0),
                LocalTime.of(14, 0), LocalTime.of(18, 0), 101L, 202L, createdAt, updatedAt);

        assertThat(schedule1)
                .isEqualTo(schedule2)
                .hasSameHashCodeAs(schedule2);
    }

    @Test
    void deveVerificarToString() {
        LocalDateTime createdAt = LocalDateTime.of(2025, 5, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 5, 2, 15, 30);

        DoctorScheduleDTO schedule = new DoctorScheduleDTO(1L, "MONDAY", LocalTime.of(8, 0), LocalTime.of(12, 0),
                LocalTime.of(14, 0), LocalTime.of(18, 0), 101L, 202L, createdAt, updatedAt);

        String toString = schedule.toString();
        assertThat(toString).contains("1", "MONDAY", "08:00", "12:00", "14:00", "18:00", "101", "202", createdAt.toString(), updatedAt.toString());
    }
}