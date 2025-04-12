package br.com.fiap.tech.scheduling.service;

import br.com.fiap.tech.scheduling.domain.Appointment;
import br.com.fiap.tech.scheduling.domain.Referral;
import br.com.fiap.tech.scheduling.dto.AppointmentRequest;
import br.com.fiap.tech.scheduling.dto.ReferralRequest;
import br.com.fiap.tech.scheduling.repository.AppointmentRepository;
import br.com.fiap.tech.scheduling.repository.ReferralRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchedulingServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ReferralRepository referralRepository;

    @InjectMocks
    private SchedulingService schedulingService;

    private AppointmentRequest appointmentRequest;
    private Appointment appointment;
    private ReferralRequest referralRequest;
    private Referral referral;

    @BeforeEach
    void setUp() {
        LocalDateTime appointmentTime = LocalDateTime.of(2025, 4, 15, 10, 0);

        appointmentRequest = new AppointmentRequest();
        appointmentRequest.setPatientId(1L);
        appointmentRequest.setDoctorId(1L);
        appointmentRequest.setFacilityId(1L);
        appointmentRequest.setAppointmentTime(appointmentTime);
        appointmentRequest.setReason("Regular checkup");

        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setPatientId(1L);
        appointment.setDoctorId(1L);
        appointment.setFacilityId(1L);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setReason("Regular checkup");
        appointment.setStatus("SCHEDULED");

        referralRequest = new ReferralRequest();
        referralRequest.setPatientId(1L);
        referralRequest.setReferringDoctorId(1L);
        referralRequest.setSpecialistDoctorId(2L);
        referralRequest.setReason("Specialist consultation needed");
        referralRequest.setUrgency("NORMAL");

        referral = new Referral();
        referral.setId(1L);
        referral.setPatientId(1L);
        referral.setReferringDoctorId(1L);
        referral.setSpecialistDoctorId(2L);
        referral.setReason("Specialist consultation needed");
        referral.setUrgency("NORMAL");
        referral.setStatus("PENDING");
    }

    @Test
    void createAppointment_Success() {
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        Appointment result = schedulingService.createAppointment(appointmentRequest);

        assertNotNull(result);
        assertEquals(appointment.getPatientId(), result.getPatientId());
        assertEquals(appointment.getAppointmentTime(), result.getAppointmentTime());
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void getAppointment_Success() {
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));

        Appointment result = schedulingService.getAppointment(1L);

        assertNotNull(result);
        assertEquals(appointment.getId(), result.getId());
        assertEquals(appointment.getReason(), result.getReason());
    }

    @Test
    void getPatientAppointments_Success() {
        when(appointmentRepository.findByPatientId(anyLong())).thenReturn(Arrays.asList(appointment));

        List<Appointment> results = schedulingService.getPatientAppointments(1L);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(appointment.getPatientId(), results.get(0).getPatientId());
    }

    @Test
    void getDoctorAppointments_Success() {
        when(appointmentRepository.findByDoctorId(anyLong())).thenReturn(Arrays.asList(appointment));

        List<Appointment> results = schedulingService.getDoctorAppointments(1L);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(appointment.getDoctorId(), results.get(0).getDoctorId());
    }

    @Test
    void createReferral_Success() {
        when(referralRepository.save(any(Referral.class))).thenReturn(referral);

        Referral result = schedulingService.createReferral(referralRequest);

        assertNotNull(result);
        assertEquals(referral.getPatientId(), result.getPatientId());
        assertEquals(referral.getReason(), result.getReason());
        verify(referralRepository).save(any(Referral.class));
    }

    @Test
    void getReferral_Success() {
        when(referralRepository.findById(anyLong())).thenReturn(Optional.of(referral));

        Referral result = schedulingService.getReferral(1L);

        assertNotNull(result);
        assertEquals(referral.getId(), result.getId());
        assertEquals(referral.getReason(), result.getReason());
    }

    @Test
    void getPatientReferrals_Success() {
        when(referralRepository.findByPatientId(anyLong())).thenReturn(Arrays.asList(referral));

        List<Referral> results = schedulingService.getPatientReferrals(1L);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(referral.getPatientId(), results.get(0).getPatientId());
    }

    @Test
    void getDoctorReferrals_Success() {
        when(referralRepository.findBySpecialistDoctorId(anyLong())).thenReturn(Arrays.asList(referral));

        List<Referral> results = schedulingService.getDoctorReferrals(2L);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(referral.getSpecialistDoctorId(), results.get(0).getSpecialistDoctorId());
    }
}
