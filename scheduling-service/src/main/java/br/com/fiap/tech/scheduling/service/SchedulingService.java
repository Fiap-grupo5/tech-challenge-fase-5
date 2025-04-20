package br.com.fiap.tech.scheduling.service;

import br.com.fiap.tech.scheduling.domain.*;
import br.com.fiap.tech.scheduling.dto.*;
import br.com.fiap.tech.scheduling.events.AppointmentCreatedEvent;
import br.com.fiap.tech.scheduling.events.ReferralCreatedEvent;
import br.com.fiap.tech.scheduling.repository.AppointmentRepository;
import br.com.fiap.tech.scheduling.repository.ReferralRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.fiap.tech.scheduling.client.FacilityClient;
import br.com.fiap.tech.scheduling.client.PeopleClient;
import br.com.fiap.tech.scheduling.dto.NearbyFacilityResponse;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulingService {

    private final AppointmentRepository appointmentRepository;
    private final ReferralRepository referralRepository;
    private final StreamBridge streamBridge;
    private final FacilityClient facilityClient;
    private final PeopleClient peopleClient;

    @Transactional
    public Appointment createAppointment(AppointmentRequest request) {
        // Validações básicas
        if (request.getDoctorId() == null) {
            throw new IllegalArgumentException("O ID do médico é obrigatório");
        }
        
        // Validar o dia da semana
        if (request.getAppointmentDay() == null || request.getAppointmentDay().trim().isEmpty()) {
            throw new IllegalArgumentException("O dia da semana é obrigatório");
        }
        
        // Normalizar o dia da semana para garantir que está no formato correto (MONDAY, TUESDAY, etc.)
        String dayOfWeek = request.getAppointmentDay().toUpperCase();
        
        // Validar que o dia da semana é válido
        try {
            java.time.DayOfWeek.valueOf(dayOfWeek);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Dia da semana inválido. Use: MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY");
        }
        
        // Calcular a data específica a partir do dia da semana (hoje ou o próximo dia correspondente)
        LocalDate today = LocalDate.now();
        LocalDate appointmentDate;
        
        if (request.getAppointmentDate() != null) {
            // Se o usuário especificou uma data, usamos ela
            appointmentDate = request.getAppointmentDate();
            
            // Validar que a data não é no passado
            if (appointmentDate.isBefore(today)) {
                throw new IllegalArgumentException("A data do agendamento não pode ser no passado");
            }
            
            // Validar que o dia da semana da data corresponde ao dia informado
            if (!appointmentDate.getDayOfWeek().name().equals(dayOfWeek)) {
                throw new IllegalArgumentException("A data especificada (" + appointmentDate + 
                    ") não corresponde ao dia da semana informado (" + dayOfWeek + ")");
            }
        } else {
            // Se o usuário não especificou uma data, calculamos a próxima data para o dia da semana informado
            java.time.DayOfWeek targetDayOfWeek = java.time.DayOfWeek.valueOf(dayOfWeek);
            appointmentDate = today;
            
            // Encontrar a próxima ocorrência do dia da semana
            while (appointmentDate.getDayOfWeek() != targetDayOfWeek) {
                appointmentDate = appointmentDate.plusDays(1);
            }
        }
        
        // Validar que o horário de término é posterior ao de início
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new IllegalArgumentException("O horário de término deve ser posterior ao horário de início");
        }
        
        // A validação de horário comercial foi removida, pois hospitais funcionam 24h
        // e o que realmente importa é se o médico tem agenda nesse horário
        
        // Verificar se o médico tem agenda neste dia da semana
        log.debug("Verificando disponibilidade do médico ID {} no dia da semana {}", request.getDoctorId(), dayOfWeek);
        List<DoctorScheduleDTO> doctorSchedules = facilityClient.getDoctorSchedules(request.getDoctorId());
        
        // Filtrar agendas para o dia da semana solicitado
        boolean hasDayAvailability = false;
        boolean hasTimeAvailability = false;
        
        for (DoctorScheduleDTO schedule : doctorSchedules) {
            if (schedule.getDayOfWeek().equals(dayOfWeek)) {
                hasDayAvailability = true;
                
                // Verificar disponibilidade no primeiro período
                if ((request.getStartTime().equals(schedule.getStartTime()) || request.getStartTime().isAfter(schedule.getStartTime())) 
                    && (request.getEndTime().equals(schedule.getEndTime()) || request.getEndTime().isBefore(schedule.getEndTime()))) {
                    hasTimeAvailability = true;
                    break;
                }
                
                // Verificar disponibilidade no segundo período, se existir
                if (schedule.getSecondPeriodStart() != null && schedule.getSecondPeriodEnd() != null) {
                    if ((request.getStartTime().equals(schedule.getSecondPeriodStart()) || request.getStartTime().isAfter(schedule.getSecondPeriodStart())) 
                        && (request.getEndTime().equals(schedule.getSecondPeriodEnd()) || request.getEndTime().isBefore(schedule.getSecondPeriodEnd()))) {
                        hasTimeAvailability = true;
                        break;
                    }
                }
            }
        }
        
        if (!hasDayAvailability) {
            throw new IllegalStateException("O médico não tem agenda disponível no dia da semana solicitado: " + dayOfWeek);
        }
        
        if (!hasTimeAvailability) {
            throw new IllegalStateException("O médico não tem horário disponível no período solicitado");
        }
        
        // Verificar se o paciente existe
        log.debug("Verificando existência do paciente com ID: {}", request.getPatientId());
        try {
            peopleClient.patientExists(request.getPatientId());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Paciente não encontrado com ID: " + request.getPatientId());
        } catch (Exception e) {
            log.error("Erro ao verificar existência do paciente: {}", e.getMessage());
            throw new ResourceAccessException("Não foi possível verificar a existência do paciente no momento");
        }
        
        // Verificar se o médico existe
        log.debug("Verificando existência do médico com ID: {}", request.getDoctorId());
        try {
            peopleClient.doctorExists(request.getDoctorId());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Médico não encontrado com ID: " + request.getDoctorId());
        } catch (Exception e) {
            log.error("Erro ao verificar existência do médico: {}", e.getMessage());
            throw new ResourceAccessException("Não foi possível verificar a existência do médico no momento");
        }
        
        // Verificar referral se informado
        if (request.getReferralId() != null) {
            log.debug("Verificando existência do encaminhamento com ID: {}", request.getReferralId());
            Referral referral = referralRepository.findById(request.getReferralId())
                .orElseThrow(() -> new EntityNotFoundException(
                    "Encaminhamento não encontrado com ID: " + request.getReferralId()));
            
            // Verificar se o referral já está agendado
            if (ReferralStatus.SCHEDULED.equals(referral.getStatus())) {
                throw new IllegalStateException("Este encaminhamento já está agendado");
            }
            
            // Verificar se o paciente do referral é o mesmo do agendamento
            if (!referral.getPatientId().equals(request.getPatientId())) {
                throw new IllegalArgumentException(
                    "O paciente do encaminhamento não corresponde ao paciente do agendamento");
            }
        }
        
        // Verificar capacidade na unidade de saúde
        if (request.getHealthcareFacilityId() != null) {
            try {
                Boolean hasAvailability = facilityClient.checkAvailabilityForDate(
                    request.getHealthcareFacilityId(), 
                    appointmentDate
                );
                
                if (!hasAvailability) {
                    throw new IllegalStateException(
                        "A unidade de saúde com ID " + request.getHealthcareFacilityId() + 
                        " não possui mais vagas disponíveis para a data " + appointmentDate
                    );
                }
            } catch (EntityNotFoundException e) {
                throw new EntityNotFoundException("Unidade de saúde não encontrada com ID: " + 
                                                 request.getHealthcareFacilityId());
            } catch (IllegalStateException e) {
                throw e; // Repassar exceção de capacidade
            } catch (Exception e) {
                log.error("Erro ao verificar disponibilidade da unidade: {}", e.getMessage());
                throw new ResourceAccessException(
                    "Não foi possível verificar a disponibilidade da unidade de saúde no momento");
            }
        }

        // Verificar conflito de horário
        if (appointmentRepository.hasConflictingAppointment(
                request.getDoctorId(),
                appointmentDate,
                request.getStartTime(),
                request.getEndTime()
        )) {
            throw new IllegalStateException("O médico já possui um agendamento neste horário");
        }

        var appointment = Appointment.builder()
                .appointmentDate(appointmentDate)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .appointmentType(request.getAppointmentType())
                .status(AppointmentStatus.SCHEDULED)
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .healthcareFacilityId(request.getHealthcareFacilityId())
                .referralId(request.getReferralId())
                .priorityLevel(request.getPriorityLevel())
                .build();

        appointment = appointmentRepository.save(appointment);
        log.info("Agendamento criado com sucesso: {}", appointment);

        // Atualizar a capacidade da unidade de saúde
        if (appointment.getHealthcareFacilityId() != null) {
            try {
                // Tentativa inicial
                try {
                    facilityClient.updateCurrentLoad(appointment.getHealthcareFacilityId(), true);
                    log.debug("Carga da unidade de saúde ID {} incrementada", appointment.getHealthcareFacilityId());
                } catch (Exception e) {
                    log.warn("Primeira tentativa de atualizar carga falhou: {}. Tentando novamente...", e.getMessage());
                    
                    // Esperar um pouco e tentar novamente
                    Thread.sleep(1000);
                    try {
                        facilityClient.updateCurrentLoad(appointment.getHealthcareFacilityId(), true);
                        log.debug("Carga da unidade de saúde ID {} incrementada na segunda tentativa", 
                            appointment.getHealthcareFacilityId());
                    } catch (Exception e2) {
                        log.error("Falha na segunda tentativa: {}", e2.getMessage());
                        
                        // Última tentativa com retry
                        Thread.sleep(2000);
                        facilityClient.updateCurrentLoad(appointment.getHealthcareFacilityId(), true);
                        log.debug("Carga da unidade de saúde ID {} incrementada na terceira tentativa", 
                            appointment.getHealthcareFacilityId());
                    }
                }
            } catch (Exception e) {
                log.error("Erro ao atualizar carga da unidade após múltiplas tentativas: {}", e.getMessage());
                // Não lançar exceção para não impedir o agendamento
            }
        }
        
        // Atualizar status do referral se necessário
        if (appointment.getReferralId() != null) {
            try {
                Referral referral = referralRepository.findById(appointment.getReferralId()).orElse(null);
                if (referral != null) {
                    referral.setStatus(ReferralStatus.SCHEDULED);
                    referralRepository.save(referral);
                    log.debug("Status do encaminhamento ID {} atualizado para SCHEDULED", 
                              appointment.getReferralId());
                }
            } catch (Exception e) {
                log.error("Erro ao atualizar status do encaminhamento: {}", e.getMessage());
                // Não lançar exceção para não impedir o agendamento
            }
        }

        // Publish appointment created event
        var event = new AppointmentCreatedEvent(
                appointment.getId(),
                appointment.getAppointmentDate(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getAppointmentType(),
                appointment.getPatientId(),
                appointment.getDoctorId(),
                appointment.getHealthcareFacilityId()
        );
        streamBridge.send("appointmentCreatedOutput-out-0", event);
        log.info("Evento de agendamento criado enviado com sucesso");

        return appointment;
    }

    @Transactional
    public Referral createReferral(ReferralRequest request) {
        // Validações básicas
        if (request.getReferralReason() == null || request.getReferralReason().trim().isEmpty()) {
            throw new IllegalArgumentException("O motivo do encaminhamento é obrigatório");
        }
        
        if (request.getReferralReason().trim().length() < 10) {
            throw new IllegalArgumentException("O motivo do encaminhamento deve ter pelo menos 10 caracteres");
        }
        
        if (request.getPriorityLevel() == null) {
            throw new IllegalArgumentException("O nível de prioridade é obrigatório");
        }
        
        if (request.getPatientId() == null) {
            throw new IllegalArgumentException("O ID do paciente é obrigatório");
        }
        
        if (request.getRequestedByDoctorId() == null) {
            throw new IllegalArgumentException("O ID do médico solicitante é obrigatório");
        }
        
        if (request.getReferralType() == null) {
            throw new IllegalArgumentException("O tipo de encaminhamento é obrigatório");
        }
        
        // Validar campos novos
        if (request.getPatientAge() != null && (request.getPatientAge() < 0 || request.getPatientAge() > 120)) {
            throw new IllegalArgumentException("Idade do paciente inválida");
        }
        
        // Verificar se o paciente existe
        log.debug("Verificando existência do paciente com ID: {}", request.getPatientId());
        peopleClient.patientExists(request.getPatientId());
        
        // Verificar se o médico existe
        log.debug("Verificando existência do médico com ID: {}", request.getRequestedByDoctorId());
        peopleClient.doctorExists(request.getRequestedByDoctorId());
        
        var referral = Referral.builder()
                .referralReason(request.getReferralReason())
                .priorityLevel(request.getPriorityLevel())
                .status(ReferralStatus.PENDING)
                .patientId(request.getPatientId())
                .requestedByDoctorId(request.getRequestedByDoctorId())
                .referralType(request.getReferralType())
                .patientAge(request.getPatientAge())
                .isPregnant(request.getIsPregnant())
                .hasMedicalUrgency(request.getHasMedicalUrgency())
                .build();

        referral = referralRepository.save(referral);
        log.info("Encaminhamento criado com sucesso: {}", referral);

        // Publish referral created event
        var event = new ReferralCreatedEvent(
                referral.getId(),
                referral.getPatientId(),
                referral.getRequestedByDoctorId(),
                referral.getReferralType(),
                referral.getPriorityLevel()
        );
        streamBridge.send("referralCreatedOutput-out-0", event);
        log.info("Evento de encaminhamento criado enviado com sucesso");

        return referral;
    }

    @Transactional
    public Appointment updateAppointmentStatus(Long id, AppointmentStatus status) {
        log.debug("Atualizando status do agendamento com ID: {} para {}", id, status);
        var appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com ID: " + id));
        
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Referral updateReferralStatus(Long id, ReferralStatus status) {
        log.debug("Atualizando status do encaminhamento com ID: {} para {}", id, status);
        // Verificar se o encaminhamento existe e obter a referência
        var referralOpt = referralRepository.findById(id);
        if (referralOpt.isEmpty()) {
            log.error("Encaminhamento não encontrado com ID: {}", id);
            throw new EntityNotFoundException("Encaminhamento não encontrado com ID: " + id);
        }
        
        var referral = referralOpt.get();
        referral.setStatus(status);
        return referralRepository.save(referral);
    }

    public List<Appointment> getPatientAppointments(Long patientId) {
        log.debug("Buscando agendamentos do paciente com ID: {}", patientId);
        
        // Verificar se o paciente existe
        peopleClient.patientExists(patientId);
        
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getDoctorAppointments(Long doctorId) {
        log.debug("Buscando agendamentos do médico com ID: {}", doctorId);
        
        // Verificar se o médico existe
        peopleClient.doctorExists(doctorId);
        
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public List<Referral> getPatientReferrals(Long patientId) {
        log.debug("Buscando encaminhamentos do paciente com ID: {}", patientId);
        
        // Verificar se o paciente existe
        peopleClient.patientExists(patientId);
        
        return referralRepository.findByPatientId(patientId);
    }

    public List<Referral> getDoctorReferrals(Long doctorId) {
        log.debug("Buscando encaminhamentos do médico com ID: {}", doctorId);
        
        // Verificar se o médico existe
        peopleClient.doctorExists(doctorId);
        
        return referralRepository.findByRequestedByDoctorId(doctorId);
    }

    public List<NearbyFacilityResponse> findNearbyFacilitiesForReferral(Double latitude, Double longitude, Double radiusInKm, Integer limit) {
        log.debug("Buscando unidades de saúde próximas às coordenadas: ({}, {}), raio: {}km, limite: {}",
                latitude, longitude, radiusInKm, limit);
                
        if (radiusInKm == null) {
            radiusInKm = 10.0;  // Valor padrão de 10km
        }
        
        if (limit == null) {
            limit = 10;  // Valor padrão de 10 resultados
        }
        
        return facilityClient.findNearbyFacilities(latitude, longitude, radiusInKm, limit);
    }

    /**
     * Cancela um agendamento existente e libera a capacidade na unidade de saúde
     */
    @Transactional
    public Appointment cancelAppointment(Long id, String cancellationReason) {
        // Verificar se o agendamento existe
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com ID: " + id));
        
        // Verificar se o agendamento já está cancelado
        if (AppointmentStatus.CANCELLED.equals(appointment.getStatus())) {
            throw new IllegalStateException("Este agendamento já está cancelado");
        }
        
        // Verificar se o agendamento já foi concluído
        if (AppointmentStatus.COMPLETED.equals(appointment.getStatus())) {
            throw new IllegalStateException("Não é possível cancelar um agendamento já concluído");
        }
        
        // Verificar se o agendamento está no futuro
        LocalDate today = LocalDate.now();
        if (appointment.getAppointmentDate().isBefore(today)) {
            throw new IllegalStateException("Não é possível cancelar um agendamento de data passada");
        }
        
        // Atualizar status para cancelado
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
        
        // Liberar a vaga na unidade
        if (appointment.getHealthcareFacilityId() != null) {
            try {
                // Tentativa inicial
                try {
                    facilityClient.updateCurrentLoad(appointment.getHealthcareFacilityId(), false);
                    log.debug("Carga da unidade de saúde ID {} decrementada", appointment.getHealthcareFacilityId());
                } catch (Exception e) {
                    log.warn("Primeira tentativa de decrementar carga falhou: {}. Tentando novamente...", e.getMessage());
                    
                    // Esperar um pouco e tentar novamente
                    Thread.sleep(1000);
                    try {
                        facilityClient.updateCurrentLoad(appointment.getHealthcareFacilityId(), false);
                        log.debug("Carga da unidade de saúde ID {} decrementada na segunda tentativa", 
                            appointment.getHealthcareFacilityId());
                    } catch (Exception e2) {
                        log.error("Falha na segunda tentativa: {}", e2.getMessage());
                        
                        // Última tentativa com retry
                        Thread.sleep(2000);
                        facilityClient.updateCurrentLoad(appointment.getHealthcareFacilityId(), false);
                        log.debug("Carga da unidade de saúde ID {} decrementada na terceira tentativa", 
                            appointment.getHealthcareFacilityId());
                    }
                }
            } catch (Exception e) {
                log.error("Erro ao atualizar carga da unidade após múltiplas tentativas: {}", e.getMessage());
                // Continuar mesmo com erro
            }
        }
        
        // Se havia um referral associado, voltar para status PENDING
        if (appointment.getReferralId() != null) {
            try {
                Referral referral = referralRepository.findById(appointment.getReferralId()).orElse(null);
                if (referral != null && ReferralStatus.SCHEDULED.equals(referral.getStatus())) {
                    referral.setStatus(ReferralStatus.PENDING);
                    referralRepository.save(referral);
                    log.debug("Status do encaminhamento ID {} revertido para PENDING", appointment.getReferralId());
                }
            } catch (Exception e) {
                log.error("Erro ao atualizar status do encaminhamento: {}", e.getMessage());
                // Continuar mesmo com erro
            }
        }
        
        log.info("Agendamento ID {} cancelado com sucesso. Motivo: {}", id, 
                 cancellationReason != null ? cancellationReason : "Não informado");
        
        return appointment;
    }
}
