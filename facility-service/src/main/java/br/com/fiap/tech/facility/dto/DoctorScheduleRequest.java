package br.com.fiap.tech.facility.dto;

import br.com.fiap.tech.facility.domain.DayOfWeek;
import br.com.fiap.tech.facility.validation.SecondPeriodValidator;
import br.com.fiap.tech.facility.validation.TimeOrderValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalTime;

@Data
@Schema(description = "Dados para criação de agenda de médico")
@TimeOrderValidator
@SecondPeriodValidator
public class DoctorScheduleRequest {
    @NotNull(message = "O dia da semana é obrigatório")
    @Schema(
        description = "Dia da semana para o agendamento",
        example = "MONDAY"
    )
    private DayOfWeek dayOfWeek;
    
    @NotNull(message = "O horário de início é obrigatório")
    @Schema(
        description = "Horário de início do primeiro período (formato HH:MM:SS)",
        example = "09:00:00"
    )
    private LocalTime startTime;
    
    @NotNull(message = "O horário de término é obrigatório")
    @Schema(
        description = "Horário de término do primeiro período (formato HH:MM:SS)",
        example = "12:00:00"
    )
    private LocalTime endTime;
    
    @Schema(
        description = "Horário de início do segundo período, se aplicável (formato HH:MM:SS)",
        example = "14:00:00"
    )
    private LocalTime secondPeriodStart;
    
    @Schema(
        description = "Horário de término do segundo período, se aplicável (formato HH:MM:SS)",
        example = "18:00:00"
    )
    private LocalTime secondPeriodEnd;
    
    @NotNull(message = "O ID do médico é obrigatório")
    @Positive(message = "O ID do médico deve ser positivo")
    @Schema(
        description = "ID do médico",
        example = "1"
    )
    private Long doctorId;
    
    @NotNull(message = "O ID da unidade de saúde é obrigatório")
    @Positive(message = "O ID da unidade de saúde deve ser positivo")
    @Schema(
        description = "ID da unidade de saúde",
        example = "1"
    )
    private Long facilityId;
}
