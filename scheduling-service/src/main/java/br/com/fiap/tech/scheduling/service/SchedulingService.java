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
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulingService {

    private final AppointmentRepository appointmentRepository;
    private final ReferralRepository referralRepository;
    private final StreamBridge streamBridge;
    private final FacilityClient facilityClient;

    @Transactional
    public Appointment createAppointment(AppointmentRequest request) {
        if (appointmentRepository.hasConflictingAppointment(
                request.getDoctorId(),
                request.getAppointmentDate(),
                request.getStartTime(),
                request.getEndTime()
        )) {
            throw new IllegalStateException("Doctor already has an appointment at this time");
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

        return appointment;
    }

    @Transactional
    public Referral createReferral(ReferralRequest request) {
        var referral = Referral.builder()
                .referralReason(request.getReferralReason())
                .priorityLevel(request.getPriorityLevel())
                .status(ReferralStatus.PENDING)
                .patientId(request.getPatientId())
                .requestedByDoctorId(request.getRequestedByDoctorId())
                .referralType(request.getReferralType())
                .build();

        referral = referralRepository.save(referral);

        // Publish referral created event
        var event = new ReferralCreatedEvent(
                referral.getId(),
                referral.getPatientId(),
                referral.getRequestedByDoctorId(),
                referral.getReferralType(),
                referral.getPriorityLevel()
        );
        streamBridge.send("referralCreatedOutput-out-0", event);

        return referral;
    }

    @Transactional
    public Appointment updateAppointmentStatus(Long id, AppointmentStatus status) {
        var appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));
        
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Referral updateReferralStatus(Long id, ReferralStatus status) {
        var referral = referralRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Referral not found"));
        
        referral.setStatus(status);
        return referralRepository.save(referral);
    }

    public List<Appointment> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public List<Referral> getPatientReferrals(Long patientId) {
        return referralRepository.findByPatientId(patientId);
    }

    public List<Referral> getDoctorReferrals(Long doctorId) {
        return referralRepository.findByRequestedByDoctorId(doctorId);
    }

    public List<NearbyFacilityResponse> findNearbyFacilitiesForReferral(Double latitude, Double longitude, Double radiusInKm, Integer limit) {
        return facilityClient.findNearbyFacilities(latitude, longitude, radiusInKm, limit);
    }
}
