package br.com.fiap.tech.scheduling.service;

import br.com.fiap.tech.scheduling.domain.Appointment;
import br.com.fiap.tech.scheduling.domain.AppointmentStatus;
import br.com.fiap.tech.scheduling.domain.AppointmentType;
import br.com.fiap.tech.scheduling.domain.PriorityLevel;
import br.com.fiap.tech.scheduling.dto.AppointmentWithPriorityDTO;
import br.com.fiap.tech.scheduling.repository.AppointmentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class AppointmentPriorityServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentPriorityService appointmentPriorityService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    private Appointment createAppointment(Long id, PriorityLevel priorityLevel, AppointmentType type, LocalDate date, Long referralId) {
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setPriorityLevel(priorityLevel);
        appointment.setAppointmentType(type);
        appointment.setAppointmentDate(date);
        appointment.setReferralId(referralId);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        return appointment;
    }

    @Test
    void shouldCalculatePriorityScore() {
        Appointment appointment = createAppointment(1L, PriorityLevel.HIGH, AppointmentType.EXAM, LocalDate.now().plusDays(2), 10L);
        int score = appointmentPriorityService.calculatePriorityScore(appointment);

        assertThat(score).isEqualTo(70 + 35 + 30 + 25); // HIGH + EXAM + 2 days + referral
    }

    @Test
    void shouldGetPrioritizedAppointments() {
        Appointment appointment1 = createAppointment(1L, PriorityLevel.HIGH, AppointmentType.EXAM, LocalDate.now().plusDays(2), 10L);
        Appointment appointment2 = createAppointment(2L, PriorityLevel.MEDIUM, AppointmentType.CONSULTATION, LocalDate.now().plusDays(5), null);

        when(appointmentRepository.findByStatus(AppointmentStatus.SCHEDULED))
                .thenReturn(Arrays.asList(appointment1, appointment2));

        List<AppointmentWithPriorityDTO> result = appointmentPriorityService.getPrioritizedAppointments();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getAppointment()).isEqualTo(appointment1);
        assertThat(result.get(1).getAppointment()).isEqualTo(appointment2);
    }

    @Test
    void shouldGetPrioritizedAppointmentsByDate() {
        LocalDate date = LocalDate.now().plusDays(3);
        Appointment appointment = createAppointment(1L, PriorityLevel.LOW, AppointmentType.CONSULTATION, date, null);

        when(appointmentRepository.findByAppointmentDateAndStatus(date, AppointmentStatus.SCHEDULED))
                .thenReturn(Collections.singletonList(appointment));

        List<AppointmentWithPriorityDTO> result = appointmentPriorityService.getPrioritizedAppointmentsByDate(date);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAppointment()).isEqualTo(appointment);
    }

    @Test
    void shouldGetNextHighestPriorityAppointment() {
        Appointment appointment1 = createAppointment(1L, PriorityLevel.HIGH, AppointmentType.EXAM, LocalDate.now().plusDays(2), 10L);
        Appointment appointment2 = createAppointment(2L, PriorityLevel.MEDIUM, AppointmentType.CONSULTATION, LocalDate.now().plusDays(5), null);

        when(appointmentRepository.findByStatus(AppointmentStatus.SCHEDULED))
                .thenReturn(Arrays.asList(appointment1, appointment2));

        AppointmentWithPriorityDTO result = appointmentPriorityService.getNextHighestPriorityAppointment();

        assertThat(result).isNotNull();
        assertThat(result.getAppointment()).isEqualTo(appointment1);
    }

    @Test
    void shouldReturnNullWhenNoAppointmentsAvailable() {
        when(appointmentRepository.findByStatus(AppointmentStatus.SCHEDULED))
                .thenReturn(Collections.emptyList());

        AppointmentWithPriorityDTO result = appointmentPriorityService.getNextHighestPriorityAppointment();

        assertThat(result).isNull();
    }
}