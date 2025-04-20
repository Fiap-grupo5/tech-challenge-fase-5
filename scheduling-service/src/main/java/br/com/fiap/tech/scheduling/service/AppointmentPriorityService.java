package br.com.fiap.tech.scheduling.service;

import br.com.fiap.tech.scheduling.domain.Appointment;
import br.com.fiap.tech.scheduling.domain.AppointmentStatus;
import br.com.fiap.tech.scheduling.domain.AppointmentType;
import br.com.fiap.tech.scheduling.domain.PriorityLevel;
import br.com.fiap.tech.scheduling.dto.AppointmentWithPriorityDTO;
import br.com.fiap.tech.scheduling.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentPriorityService {
    private final AppointmentRepository appointmentRepository;
    
    /**
     * Calcula a pontuação de prioridade para um agendamento
     */
    public int calculatePriorityScore(Appointment appointment) {
        int score = 0;
        
        // Base score por nível de prioridade (se tiver)
        if (appointment.getPriorityLevel() != null) {
            switch (appointment.getPriorityLevel()) {
                case URGENT:
                    score += 100;
                    break;
                case HIGH:
                    score += 70;
                    break;
                case MEDIUM:
                    score += 40;
                    break;
                case LOW:
                    score += 10;
                    break;
            }
        } else {
            // Agendamento sem prioridade explícita
            score += 10; // Prioridade baixa padrão
        }
        
        // Adicionar pontos pelo tipo de agendamento
        if (appointment.getAppointmentType() == AppointmentType.EXAM) {
            score += 35; // Exames têm prioridade mais alta
        } else if (appointment.getAppointmentType() == AppointmentType.CONSULTATION) {
            score += 20; // Consultas têm prioridade padrão
        }
        
        // Considerar a proximidade da data do agendamento
        long daysUntilAppointment = ChronoUnit.DAYS.between(LocalDate.now(), appointment.getAppointmentDate());
        if (daysUntilAppointment <= 3) {
            score += 30; // Alta prioridade para agendamentos próximos
        } else if (daysUntilAppointment <= 7) {
            score += 15; // Prioridade média para agendamentos na próxima semana
        }
        
        // Se o agendamento foi gerado a partir de um encaminhamento, adicionar pontos
        if (appointment.getReferralId() != null) {
            score += 25; // Agendamentos de encaminhamentos têm maior prioridade
        }
        
        return score;
    }
    
    /**
     * Obtém todos os agendamentos agendados ordenados por prioridade
     */
    public List<AppointmentWithPriorityDTO> getPrioritizedAppointments() {
        // Buscar todos os agendamentos com status SCHEDULED
        List<Appointment> scheduledAppointments = appointmentRepository.findByStatus(AppointmentStatus.SCHEDULED);
        
        // Calcular pontuação de prioridade e ordenar
        return scheduledAppointments.stream()
            .map(app -> new AppointmentWithPriorityDTO(app, calculatePriorityScore(app)))
            .sorted(Comparator.comparing(AppointmentWithPriorityDTO::getPriorityScore).reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * Obtém agendamentos para uma data específica, ordenados por prioridade
     */
    public List<AppointmentWithPriorityDTO> getPrioritizedAppointmentsByDate(LocalDate date) {
        // Buscar agendamentos agendados para a data especificada
        List<Appointment> scheduledAppointments = 
            appointmentRepository.findByAppointmentDateAndStatus(date, AppointmentStatus.SCHEDULED);
        
        // Calcular pontuação de prioridade e ordenar
        return scheduledAppointments.stream()
            .map(app -> new AppointmentWithPriorityDTO(app, calculatePriorityScore(app)))
            .sorted(Comparator.comparing(AppointmentWithPriorityDTO::getPriorityScore).reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * Obtém o próximo agendamento com maior prioridade
     */
    public AppointmentWithPriorityDTO getNextHighestPriorityAppointment() {
        List<AppointmentWithPriorityDTO> prioritizedList = getPrioritizedAppointments();
        
        if (prioritizedList.isEmpty()) {
            return null;
        }
        
        return prioritizedList.get(0);
    }
} 