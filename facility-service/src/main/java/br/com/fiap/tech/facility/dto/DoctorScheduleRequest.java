package br.com.fiap.tech.facility.dto;

import br.com.fiap.tech.facility.domain.DayOfWeek;
import lombok.Data;

import java.time.LocalTime;

@Data
public class DoctorScheduleRequest {
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime secondPeriodStart;
    private LocalTime secondPeriodEnd;
    private Long doctorId;
    private Long facilityId;
}
