package br.com.fiap.tech.scheduling.dto;

import br.com.fiap.tech.scheduling.domain.AppointmentType;
import br.com.fiap.tech.scheduling.domain.PriorityLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Schema(description = "Dados para criação de agendamento")
public class AppointmentRequest {
    @Schema(
        description = "Dia da semana para o agendamento (MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY)",
        example = "MONDAY"
    )
    private String appointmentDay;
    
    @Schema(
        description = "Data específica do agendamento (opcional, usado apenas para referência de data específica)",
        example = "2023-12-20"
    )
    private LocalDate appointmentDate;
    
    @Schema(
        description = "Horário de início da consulta",
        example = "10:00:00"
    )
    private LocalTime startTime;
    
    @Schema(
        description = "Horário de término da consulta",
        example = "11:00:00"
    )
    private LocalTime endTime;
    
    @Schema(
        description = "Tipo do agendamento (CONSULTATION ou EXAM)",
        example = "CONSULTATION"
    )
    private AppointmentType appointmentType;
    
    @Schema(
        description = "ID do paciente",
        example = "1"
    )
    private Long patientId;
    
    @Schema(
        description = "ID do médico",
        example = "1"
    )
    private Long doctorId;
    
    @Schema(
        description = "ID da unidade de saúde",
        example = "1"
    )
    private Long healthcareFacilityId;
    
    @Schema(
        description = "ID do encaminhamento (quando existente)",
        example = "1"
    )
    private Long referralId;
    
    @Schema(
        description = "Nível de prioridade do agendamento (LOW, MEDIUM, HIGH, URGENT)",
        example = "MEDIUM"
    )
    private PriorityLevel priorityLevel;
}
