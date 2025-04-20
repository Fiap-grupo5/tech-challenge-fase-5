package br.com.fiap.tech.scheduling.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DoctorScheduleDTO {
    private Long id;
    private String dayOfWeek;  // MONDAY, TUESDAY, etc.
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime secondPeriodStart;
    private LocalTime secondPeriodEnd;
    private Long doctorId;
    private Long facilityId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 