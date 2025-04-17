package br.com.fiap.tech.facility.dto;

import br.com.fiap.tech.facility.domain.DayOfWeek;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalTime;

@Data
public class DoctorScheduleRequest {
    @Schema(example = "MONDAY", description = "Dia da semana para o agendamento")
    private DayOfWeek dayOfWeek;
    
    @Schema(example = "09:00:00", description = "Horário de início do primeiro período (formato HH:MM:SS)")
    private LocalTime startTime;
    
    @Schema(example = "12:00:00", description = "Horário de término do primeiro período (formato HH:MM:SS)")
    private LocalTime endTime;
    
    @Schema(example = "14:00:00", description = "Horário de início do segundo período, se aplicável (formato HH:MM:SS)")
    private LocalTime secondPeriodStart;
    
    @Schema(example = "18:00:00", description = "Horário de término do segundo período, se aplicável (formato HH:MM:SS)")
    private LocalTime secondPeriodEnd;
    
    @Schema(example = "1", description = "ID do médico")
    private Long doctorId;
    
    @Schema(example = "1", description = "ID da unidade de saúde")
    private Long facilityId;
}
