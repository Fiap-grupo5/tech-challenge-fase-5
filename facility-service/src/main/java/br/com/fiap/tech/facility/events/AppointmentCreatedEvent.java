package br.com.fiap.tech.facility.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCreatedEvent {
    private Long appointmentId;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String appointmentType;
    private Long patientId;
    private Long doctorId;
    private Long healthcareFacilityId;
}
