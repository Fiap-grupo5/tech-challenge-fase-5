package br.com.fiap.tech.scheduling.dto;

import br.com.fiap.tech.scheduling.domain.AppointmentType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequest {
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private AppointmentType appointmentType;
    private Long patientId;
    private Long doctorId;
    private Long healthcareFacilityId;
    private Long referralId;
}
