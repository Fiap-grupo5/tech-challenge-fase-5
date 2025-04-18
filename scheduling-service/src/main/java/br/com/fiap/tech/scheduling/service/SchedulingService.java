package br.com.fiap.tech.scheduling.service;

import br.com.fiap.tech.scheduling.domain.*;
import br.com.fiap.tech.scheduling.dto.AppointmentRequest;
import br.com.fiap.tech.scheduling.dto.ReferralRequest;
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
        if (request.getAppointmentDate() == null) {
            throw new IllegalArgumentException("A data do agendamento é obrigatória");
        }
        
        if (request.getStartTime() == null) {
            throw new IllegalArgumentException("O horário de início é obrigatório");
        }
        
        if (request.getEndTime() == null) {
            throw new IllegalArgumentException("O horário de término é obrigatório");
        }
        
        if (request.getPatientId() == null) {
            throw new IllegalArgumentException("O ID do paciente é obrigatório");
        }
        
        if (request.getDoctorId() == null) {
            throw new IllegalArgumentException("O ID do médico é obrigatório");
        }
        
        // Validar que a data não é no passado
        if (request.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data do agendamento não pode ser no passado");
        }
        
        // Validar que o horário de término é posterior ao de início
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new IllegalArgumentException("O horário de término deve ser posterior ao horário de início");
        }
        
        // Validar que o agendamento está dentro do horário comercial (8h às 18h)
        if (request.getStartTime().isBefore(LocalTime.of(8, 0)) || 
            request.getEndTime().isAfter(LocalTime.of(18, 0))) {
            throw new IllegalArgumentException("O agendamento deve estar dentro do horário comercial (8h às 18h)");
        }
        
        // Verificar se o paciente existe
        log.debug("Verificando existência do paciente com ID: {}", request.getPatientId());
        peopleClient.patientExists(request.getPatientId());
        
        // Verificar se o médico existe
        log.debug("Verificando existência do médico com ID: {}", request.getDoctorId());
        peopleClient.doctorExists(request.getDoctorId());
        
        // Verificar se o referral existe, se informado
        if (request.getReferralId() != null) {
            log.debug("Verificando existência do encaminhamento com ID: {}", request.getReferralId());
            referralRepository.findById(request.getReferralId())
                .orElseThrow(() -> new EntityNotFoundException("Encaminhamento não encontrado com ID: " + request.getReferralId()));
        }

        // Verificar conflito de horário
        if (appointmentRepository.hasConflictingAppointment(
                request.getDoctorId(),
                request.getAppointmentDate(),
                request.getStartTime(),
                request.getEndTime()
        )) {
            throw new IllegalStateException("O médico já possui um agendamento neste horário");
        }

        var appointment = Appointment.builder()
                .appointmentDate(request.getAppointmentDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .appointmentType(request.getAppointmentType())
                .status(AppointmentStatus.SCHEDULED)
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .healthcareFacilityId(request.getHealthcareFacilityId())
                .referralId(request.getReferralId())
                .build();

        appointment = appointmentRepository.save(appointment);
        log.info("Agendamento criado com sucesso: {}", appointment);

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
}
