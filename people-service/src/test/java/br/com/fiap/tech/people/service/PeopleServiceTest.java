package br.com.fiap.tech.people.service;

import br.com.fiap.tech.people.domain.Doctor;
import br.com.fiap.tech.people.domain.Patient;
import br.com.fiap.tech.people.dto.DoctorRequest;
import br.com.fiap.tech.people.dto.PatientRequest;
import br.com.fiap.tech.people.repository.DoctorRepository;
import br.com.fiap.tech.people.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeopleServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private PeopleService peopleService;

    private PatientRequest patientRequest;
    private Patient patient;
    private DoctorRequest doctorRequest;
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        patientRequest = new PatientRequest();
        patientRequest.setUserId(1L);
        patientRequest.setFirstName("John");
        patientRequest.setLastName("Doe");
        patientRequest.setCpf("12345678901");
        patientRequest.setBirthDate(LocalDate.of(1990, 1, 1));
        patientRequest.setGender("MALE");
        patientRequest.setPhoneNumber("11999990001");

        patient = new Patient();
        patient.setId(1L);
        patient.setUserId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setCpf("12345678901");
        patient.setBirthDate(LocalDate.of(1990, 1, 1));
        patient.setGender("MALE");
        patient.setPhoneNumber("11999990001");

        doctorRequest = new DoctorRequest();
        doctorRequest.setUserId(2L);
        doctorRequest.setFirstName("Jane");
        doctorRequest.setLastName("Smith");
        doctorRequest.setCrm("CRM123456");
        doctorRequest.setSpecialty("Cardiology");
        doctorRequest.setPhoneNumber("11999990002");

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setUserId(2L);
        doctor.setFirstName("Jane");
        doctor.setLastName("Smith");
        doctor.setCrm("CRM123456");
        doctor.setSpecialty("Cardiology");
        doctor.setPhoneNumber("11999990002");
    }

    @Test
    void createPatient_Success() {
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        Patient result = peopleService.createPatient(patientRequest);

        assertNotNull(result);
        assertEquals(patient.getFirstName(), result.getFirstName());
        assertEquals(patient.getCpf(), result.getCpf());
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void getPatient_Success() {
        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));

        Patient result = peopleService.getPatient(1L);

        assertNotNull(result);
        assertEquals(patient.getId(), result.getId());
        assertEquals(patient.getFirstName(), result.getFirstName());
    }

    @Test
    void getAllPatients_Success() {
        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient));

        List<Patient> results = peopleService.getAllPatients();

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(patient.getFirstName(), results.get(0).getFirstName());
    }

    @Test
    void createDoctor_Success() {
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        Doctor result = peopleService.createDoctor(doctorRequest);

        assertNotNull(result);
        assertEquals(doctor.getFirstName(), result.getFirstName());
        assertEquals(doctor.getCrm(), result.getCrm());
        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    void getDoctor_Success() {
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        Doctor result = peopleService.getDoctor(1L);

        assertNotNull(result);
        assertEquals(doctor.getId(), result.getId());
        assertEquals(doctor.getFirstName(), result.getFirstName());
    }

    @Test
    void getAllDoctors_Success() {
        when(doctorRepository.findAll()).thenReturn(Arrays.asList(doctor));

        List<Doctor> results = peopleService.getAllDoctors();

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(doctor.getFirstName(), results.get(0).getFirstName());
    }

    @Test
    void getDoctorsBySpecialty_Success() {
        when(doctorRepository.findBySpecialty(anyString())).thenReturn(Arrays.asList(doctor));

        List<Doctor> results = peopleService.getDoctorsBySpecialty("Cardiology");

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("Cardiology", results.get(0).getSpecialty());
    }
}
