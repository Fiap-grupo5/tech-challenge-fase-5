package br.com.fiap.tech.people.service;

import br.com.fiap.tech.people.domain.Administrator;
import br.com.fiap.tech.people.domain.Doctor;
import br.com.fiap.tech.people.domain.Patient;
import br.com.fiap.tech.people.events.UserCreatedEvent;
import br.com.fiap.tech.people.repository.AdministratorRepository;
import br.com.fiap.tech.people.repository.DoctorRepository;
import br.com.fiap.tech.people.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PeopleServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AdministratorRepository administratorRepository;

    @Captor
    private ArgumentCaptor<Patient> patientCaptor;

    @Captor
    private ArgumentCaptor<Doctor> doctorCaptor;

    @Captor
    private ArgumentCaptor<Administrator> administratorCaptor;

    private PeopleService peopleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        peopleService = new PeopleService(patientRepository, doctorRepository, administratorRepository);

        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> {
            Patient patient = invocation.getArgument(0);
            patient.setId(1L);
            return patient;
        });

        when(doctorRepository.save(any(Doctor.class))).thenAnswer(invocation -> {
            Doctor doctor = invocation.getArgument(0);
            doctor.setId(1L);
            return doctor;
        });

        when(administratorRepository.save(any(Administrator.class))).thenAnswer(invocation -> {
            Administrator administrator = invocation.getArgument(0);
            administrator.setId(1L);
            return administrator;
        });
    }

    @Test
    @DisplayName("Deve criar um paciente a partir de um evento")
    void shouldCreatePatientFromEvent() {
        // Given
        UserCreatedEvent event = new UserCreatedEvent();
        event.setUserId(1L);
        event.setUsername("patient@example.com");
        event.setUserType("PATIENT");
        event.setFullName("João Silva");
        event.setCpf("12345678900");
        event.setEmail("patient@example.com");
        event.setNationalHealthCard("123456789");
        event.setPhoneNumber("11999999999");
        event.setBirthDate(LocalDate.of(1990, 1, 1));
        event.setAddress("Rua Exemplo, 123");
        event.setCity("São Paulo");
        event.setState("SP");
        event.setZipCode("01234567");

        // When
        peopleService.createPatient(event);

        // Then
        verify(patientRepository).save(patientCaptor.capture());
        Patient patient = patientCaptor.getValue();
        
        assertEquals(1L, patient.getUserId());
        assertEquals("João Silva", patient.getFullName());
        assertEquals("12345678900", patient.getCpf());
        assertEquals("patient@example.com", patient.getEmail());
        assertEquals("123456789", patient.getNationalHealthCard());
        assertEquals("11999999999", patient.getPhoneNumber());
        assertEquals(LocalDate.of(1990, 1, 1), patient.getBirthDate());
        assertEquals("Rua Exemplo, 123", patient.getAddress());
        assertEquals("São Paulo", patient.getCity());
        assertEquals("SP", patient.getState());
        assertEquals("01234567", patient.getZipCode());
    }

    @Test
    @DisplayName("Deve criar um paciente mesmo sem nationalHealthCard")
    void shouldCreatePatientEvenWithoutNationalHealthCard() {
        // Given
        UserCreatedEvent event = new UserCreatedEvent();
        event.setUserId(1L);
        event.setUsername("patient@example.com");
        event.setUserType("PATIENT");
        event.setFullName("João Silva");
        event.setCpf("12345678900");
        event.setEmail("patient@example.com");
        event.setPhoneNumber("11999999999");

        // When
        peopleService.createPatient(event);

        // Then
        verify(patientRepository).save(patientCaptor.capture());
        Patient patient = patientCaptor.getValue();
        
        assertEquals(1L, patient.getUserId());
        assertEquals("João Silva", patient.getFullName());
        assertEquals("12345678900", patient.getCpf());
        assertEquals("patient@example.com", patient.getEmail());
        assertNull(patient.getNationalHealthCard());
    }

    @Test
    @DisplayName("Deve criar um médico a partir de um evento")
    void shouldCreateDoctorFromEvent() {
        // Given
        UserCreatedEvent event = new UserCreatedEvent();
        event.setUserId(1L);
        event.setUsername("doctor@example.com");
        event.setUserType("DOCTOR");
        event.setFullName("Dr. Carlos Oliveira");
        event.setCpf("98765432100");
        event.setEmail("doctor@example.com");
        event.setCrm("CRM/SP 12345");
        event.setSpecialty("Cardiologia");
        event.setPhoneNumber("11988888888");

        // When
        peopleService.createDoctor(event);

        // Then
        verify(doctorRepository).save(doctorCaptor.capture());
        Doctor doctor = doctorCaptor.getValue();
        
        assertEquals(1L, doctor.getUserId());
        assertEquals("Dr. Carlos Oliveira", doctor.getFullName());
        assertEquals("98765432100", doctor.getCpf());
        assertEquals("doctor@example.com", doctor.getEmail());
        assertEquals("CRM/SP 12345", doctor.getCrm());
        assertEquals("Cardiologia", doctor.getSpecialty());
        assertEquals("11988888888", doctor.getPhoneNumber());
    }

    @Test
    @DisplayName("Deve criar um médico com valores padrão se faltarem campos específicos")
    void shouldCreateDoctorWithDefaultValuesIfMissingFields() {
        // Given
        UserCreatedEvent event = new UserCreatedEvent();
        event.setUserId(1L);
        event.setUsername("doctor@example.com");
        event.setUserType("DOCTOR");
        event.setFullName("Dr. Carlos Oliveira");
        event.setCpf("98765432100");
        event.setEmail("doctor@example.com");
        // CRM e specialty ausentes

        // When
        peopleService.createDoctor(event);

        // Then
        verify(doctorRepository).save(doctorCaptor.capture());
        Doctor doctor = doctorCaptor.getValue();
        
        assertEquals(1L, doctor.getUserId());
        assertEquals("Dr. Carlos Oliveira", doctor.getFullName());
        assertEquals("98765432100", doctor.getCpf());
        assertEquals("doctor@example.com", doctor.getEmail());
        assertEquals("PENDENTE", doctor.getCrm());
        assertEquals("GERAL", doctor.getSpecialty());
    }

    @Test
    @DisplayName("Deve criar um administrador a partir de um evento")
    void shouldCreateAdministratorFromEvent() {
        // Given
        UserCreatedEvent event = new UserCreatedEvent();
        event.setUserId(1L);
        event.setUsername("admin@example.com");
        event.setUserType("ADMINISTRATOR");
        event.setFullName("Admin Silva");
        event.setCpf("11122233344");
        event.setEmail("admin@example.com");
        event.setPhoneNumber("11977777777");

        // When
        peopleService.createAdministrator(event);

        // Then
        verify(administratorRepository).save(administratorCaptor.capture());
        Administrator administrator = administratorCaptor.getValue();
        
        assertEquals(1L, administrator.getUserId());
        assertEquals("Admin Silva", administrator.getFullName());
        assertEquals("11122233344", administrator.getCpf());
        assertEquals("admin@example.com", administrator.getEmail());
        assertEquals("11977777777", administrator.getPhoneNumber());
    }

    @Test
    @DisplayName("Deve processar corretamente evento de usuário criado para diferentes tipos")
    void shouldHandleUserCreatedEventForDifferentTypes() {
        // Teste para PATIENT
        UserCreatedEvent patientEvent = new UserCreatedEvent();
        patientEvent.setUserId(1L);
        patientEvent.setUsername("patient@example.com");
        patientEvent.setUserType("PATIENT");
        patientEvent.setFullName("João Silva");
        patientEvent.setCpf("12345678900");
        patientEvent.setEmail("patient@example.com");
        
        peopleService.handleUserCreated(patientEvent);
        verify(patientRepository).save(any(Patient.class));
        
        // Teste para DOCTOR
        UserCreatedEvent doctorEvent = new UserCreatedEvent();
        doctorEvent.setUserId(2L);
        doctorEvent.setUsername("doctor@example.com");
        doctorEvent.setUserType("DOCTOR");
        doctorEvent.setFullName("Dr. Carlos Oliveira");
        doctorEvent.setCpf("98765432100");
        doctorEvent.setEmail("doctor@example.com");
        
        peopleService.handleUserCreated(doctorEvent);
        verify(doctorRepository).save(any(Doctor.class));
        
        // Teste para ADMINISTRATOR
        UserCreatedEvent adminEvent = new UserCreatedEvent();
        adminEvent.setUserId(3L);
        adminEvent.setUsername("admin@example.com");
        adminEvent.setUserType("ADMINISTRATOR");
        adminEvent.setFullName("Admin Silva");
        adminEvent.setCpf("11122233344");
        adminEvent.setEmail("admin@example.com");
        
        peopleService.handleUserCreated(adminEvent);
        verify(administratorRepository).save(any(Administrator.class));
    }
} 