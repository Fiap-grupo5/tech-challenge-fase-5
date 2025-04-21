package br.com.fiap.tech.scheduling.dto;

import br.com.fiap.tech.scheduling.domain.AppointmentType;
import br.com.fiap.tech.scheduling.domain.PriorityLevel;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class AppointmentRequestTest {

    @Test
    void shouldSetAndGetFields() {
        AppointmentRequest request = new AppointmentRequest();
        request.setAppointmentDay("MONDAY");
        request.setAppointmentDate(LocalDate.of(2023, 12, 20));
        request.setStartTime(LocalTime.of(10, 0));
        request.setEndTime(LocalTime.of(11, 0));
        request.setAppointmentType(AppointmentType.CONSULTATION);
        request.setPatientId(1L);
        request.setDoctorId(2L);
        request.setHealthcareFacilityId(3L);
        request.setReferralId(4L);
        request.setPriorityLevel(PriorityLevel.MEDIUM);

        assertThat(request.getAppointmentDay()).isEqualTo("MONDAY");
        assertThat(request.getAppointmentDate()).isEqualTo(LocalDate.of(2023, 12, 20));
        assertThat(request.getStartTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(request.getEndTime()).isEqualTo(LocalTime.of(11, 0));
        assertThat(request.getAppointmentType()).isEqualTo(AppointmentType.CONSULTATION);
        assertThat(request.getPatientId()).isEqualTo(1L);
        assertThat(request.getDoctorId()).isEqualTo(2L);
        assertThat(request.getHealthcareFacilityId()).isEqualTo(3L);
        assertThat(request.getReferralId()).isEqualTo(4L);
        assertThat(request.getPriorityLevel()).isEqualTo(PriorityLevel.MEDIUM);
    }

    @Test
    void shouldVerifyEqualsAndHashCode() {
        AppointmentRequest request1 = new AppointmentRequest();
        request1.setAppointmentDay("MONDAY");
        request1.setAppointmentDate(LocalDate.of(2023, 12, 20));
        request1.setStartTime(LocalTime.of(10, 0));
        request1.setEndTime(LocalTime.of(11, 0));
        request1.setAppointmentType(AppointmentType.CONSULTATION);
        request1.setPatientId(1L);
        request1.setDoctorId(2L);
        request1.setHealthcareFacilityId(3L);
        request1.setReferralId(4L);
        request1.setPriorityLevel(PriorityLevel.MEDIUM);

        AppointmentRequest request2 = new AppointmentRequest();
        request2.setAppointmentDay("MONDAY");
        request2.setAppointmentDate(LocalDate.of(2023, 12, 20));
        request2.setStartTime(LocalTime.of(10, 0));
        request2.setEndTime(LocalTime.of(11, 0));
        request2.setAppointmentType(AppointmentType.CONSULTATION);
        request2.setPatientId(1L);
        request2.setDoctorId(2L);
        request2.setHealthcareFacilityId(3L);
        request2.setReferralId(4L);
        request2.setPriorityLevel(PriorityLevel.MEDIUM);

        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    void shouldVerifyToString() {
        AppointmentRequest request = new AppointmentRequest();
        request.setAppointmentDay("MONDAY");
        request.setAppointmentDate(LocalDate.of(2023, 12, 20));
        request.setStartTime(LocalTime.of(10, 0));
        request.setEndTime(LocalTime.of(11, 0));
        request.setAppointmentType(AppointmentType.CONSULTATION);
        request.setPatientId(1L);
        request.setDoctorId(2L);
        request.setHealthcareFacilityId(3L);
        request.setReferralId(4L);
        request.setPriorityLevel(PriorityLevel.MEDIUM);

        String toString = request.toString();
        assertThat(toString).contains("MONDAY", "2023-12-20", "10:00", "11:00", "CONSULTATION", "1", "2", "3", "4", "MEDIUM");
    }
}