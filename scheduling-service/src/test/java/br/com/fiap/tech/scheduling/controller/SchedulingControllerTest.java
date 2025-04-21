package br.com.fiap.tech.scheduling.controller;

import br.com.fiap.tech.scheduling.domain.*;
import br.com.fiap.tech.scheduling.dto.*;
import br.com.fiap.tech.scheduling.service.SchedulingService;
import br.com.fiap.tech.scheduling.service.PriorityService;
import br.com.fiap.tech.scheduling.service.AppointmentPriorityService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SchedulingControllerTest {

    @Mock
    private SchedulingService schedulingService;

    @Mock
    private PriorityService priorityService;

    @Mock
    private AppointmentPriorityService appointmentPriorityService;

    @InjectMocks
    private SchedulingController schedulingController;

    public SchedulingControllerTest() {
        try (AutoCloseable mocks = MockitoAnnotations.openMocks(this)) {
        } catch (Exception e) {
            throw new RuntimeException("Falha ao inicializar os mocks", e);
        }
    }

    @Test
    void shouldCreateAppointment() {
        AppointmentRequest request = new AppointmentRequest();
        Appointment appointment = new Appointment();
        when(schedulingService.createAppointment(request)).thenReturn(appointment);

        ResponseEntity<Appointment> response = schedulingController.createAppointment(request);

        assertThat(response.getBody()).isEqualTo(appointment);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        verify(schedulingService).createAppointment(request);
    }

    @Test
    void shouldCreateReferral() {
        ReferralRequest request = new ReferralRequest();
        Referral referral = new Referral();
        when(schedulingService.createReferral(request)).thenReturn(referral);

        ResponseEntity<Referral> response = schedulingController.createReferral(request);

        assertThat(response.getBody()).isEqualTo(referral);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        verify(schedulingService).createReferral(request);
    }

    @Test
    void shouldUpdateAppointmentStatus() {
        Long id = 1L;
        AppointmentStatus status = AppointmentStatus.CONFIRMED;
        Appointment appointment = new Appointment();
        when(schedulingService.updateAppointmentStatus(id, status)).thenReturn(appointment);

        ResponseEntity<Appointment> response = schedulingController.updateAppointmentStatus(id, status);

        assertThat(response.getBody()).isEqualTo(appointment);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        verify(schedulingService).updateAppointmentStatus(id, status);
    }

    @Test
    void shouldGetPatientAppointments() {
        Long patientId = 1L;
        List<Appointment> appointments = Collections.emptyList();
        when(schedulingService.getPatientAppointments(patientId)).thenReturn(appointments);

        ResponseEntity<List<Appointment>> response = schedulingController.getPatientAppointments(patientId);

        assertThat(response.getBody()).isEqualTo(appointments);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        verify(schedulingService).getPatientAppointments(patientId);
    }

    @Test
    void shouldGetPrioritizedAppointmentsByDate() {
        LocalDate date = LocalDate.now();
        List<AppointmentWithPriorityDTO> appointments = Collections.emptyList();
        when(appointmentPriorityService.getPrioritizedAppointmentsByDate(date)).thenReturn(appointments);

        ResponseEntity<List<AppointmentWithPriorityDTO>> response = schedulingController.getPrioritizedAppointmentsByDate(date);

        assertThat(response.getBody()).isEqualTo(appointments);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        verify(appointmentPriorityService).getPrioritizedAppointmentsByDate(date);
    }
}