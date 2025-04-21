package br.com.fiap.tech.scheduling.service;

import br.com.fiap.tech.scheduling.domain.*;
import br.com.fiap.tech.scheduling.dto.AppointmentRequest;
import br.com.fiap.tech.scheduling.dto.DoctorScheduleDTO;
import br.com.fiap.tech.scheduling.dto.ReferralRequest;
import br.com.fiap.tech.scheduling.events.AppointmentCreatedEvent;
import br.com.fiap.tech.scheduling.events.ReferralCreatedEvent;
import br.com.fiap.tech.scheduling.repository.AppointmentRepository;
import br.com.fiap.tech.scheduling.repository.ReferralRepository;
import br.com.fiap.tech.scheduling.client.FacilityClient;
import br.com.fiap.tech.scheduling.client.PeopleClient;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.stream.function.StreamBridge;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SchedulingServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ReferralRepository referralRepository;

    @Mock
    private FacilityClient facilityClient;

    @Mock
    private PeopleClient peopleClient;

    @Mock
    private StreamBridge streamBridge;

    @InjectMocks
    private SchedulingService schedulingService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void shouldCreateAppointmentSuccessfully() {
        AppointmentRequest request = new AppointmentRequest();
        request.setDoctorId(1L);
        request.setPatientId(2L);
        request.setAppointmentDay("MONDAY");
        request.setStartTime(LocalTime.of(10, 0));
        request.setEndTime(LocalTime.of(11, 0));
        request.setAppointmentType(AppointmentType.CONSULTATION);

        DoctorScheduleDTO schedule = new DoctorScheduleDTO();
        schedule.setDayOfWeek("MONDAY");
        schedule.setStartTime(LocalTime.of(8, 0));
        schedule.setEndTime(LocalTime.of(18, 0));
        when(facilityClient.getDoctorSchedules(1L)).thenReturn(Collections.singletonList(schedule));

        when(peopleClient.patientExists(2L)).thenReturn(true);
        when(peopleClient.doctorExists(1L)).thenReturn(true);
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Appointment appointment = schedulingService.createAppointment(request);

        assertThat(appointment).isNotNull();
        assertThat(appointment.getDoctorId()).isEqualTo(1L);
        assertThat(appointment.getPatientId()).isEqualTo(2L);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void shouldThrowExceptionWhenDoctorIdIsNull() {
        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId(2L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            schedulingService.createAppointment(request);
        });

        assertThat(exception.getMessage()).isEqualTo("O ID do médico é obrigatório");
    }

    @Test
    void shouldCreateReferralSuccessfully() {
        ReferralRequest request = new ReferralRequest();
        request.setReferralReason("Consulta especializada");
        request.setPriorityLevel(PriorityLevel.HIGH);
        request.setPatientId(2L);
        request.setRequestedByDoctorId(1L);
        request.setReferralType(ReferralType.SPECIALIST);

        when(peopleClient.patientExists(2L)).thenReturn(true);
        when(peopleClient.doctorExists(1L)).thenReturn(true);
        when(referralRepository.save(any(Referral.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(streamBridge.send(eq("referralCreatedOutput-out-0"), any(ReferralCreatedEvent.class))).thenReturn(true);

        Referral referral = schedulingService.createReferral(request);

        assertThat(referral).isNotNull();
        assertThat(referral.getReferralReason()).isEqualTo("Consulta especializada");
        verify(referralRepository, times(1)).save(any(Referral.class));
        verify(streamBridge, times(1)).send(eq("referralCreatedOutput-out-0"), any(ReferralCreatedEvent.class));
    }

    @Test
    void shouldUpdateAppointmentStatus() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Appointment updatedAppointment = schedulingService.updateAppointmentStatus(1L, AppointmentStatus.CANCELLED);

        assertThat(updatedAppointment.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);
        verify(appointmentRepository, times(1)).save(appointment);
    }

    @Test
    void shouldThrowExceptionWhenAppointmentNotFound() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            schedulingService.updateAppointmentStatus(1L, AppointmentStatus.CANCELLED);
        });

        assertThat(exception.getMessage()).isEqualTo("Agendamento não encontrado com ID: 1");
    }

    @Test
    void shouldCancelAppointmentSuccessfully() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setAppointmentDate(LocalDate.now().plusDays(1));
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Appointment cancelledAppointment = schedulingService.cancelAppointment(1L, "Paciente indisponível");

        assertThat(cancelledAppointment.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);
        verify(appointmentRepository, times(1)).save(appointment);
    }

    @Test
    void shouldThrowExceptionWhenCancellingPastAppointment() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setAppointmentDate(LocalDate.now().minusDays(1));
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            schedulingService.cancelAppointment(1L, "Paciente indisponível");
        });

        assertThat(exception.getMessage()).isEqualTo("Não é possível cancelar um agendamento de data passada");
    }
}