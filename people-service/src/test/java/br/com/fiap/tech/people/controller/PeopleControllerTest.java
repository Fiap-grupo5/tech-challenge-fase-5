package br.com.fiap.tech.people.controller;

import br.com.fiap.tech.people.domain.*;
import br.com.fiap.tech.people.dto.*;
import br.com.fiap.tech.people.service.PeopleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PeopleControllerTest {

    @Mock
    private PeopleService peopleService;

    @InjectMocks
    private PeopleController peopleController;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void shouldReturnPatientWhenFound() {
        Long patientId = 1L;
        Patient patient = new Patient();
        patient.setId(patientId);
        when(peopleService.getPatient(patientId)).thenReturn(patient);

        ResponseEntity<Patient> response = peopleController.getPatient(patientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo");
        assertEquals(patient, response.getBody());
        verify(peopleService, times(1)).getPatient(patientId);
    }

    @Test
    void shouldUpdatePatientSuccessfully() {
        Long patientId = 1L;
        PatientRequest request = new PatientRequest();
        request.setFullName("John Doe");
        request.setCpf("12345678900");

        doNothing().when(peopleService).updatePatient(any(UpdatePatientRequest.class));

        ResponseEntity<Void> response = peopleController.updatePatient(patientId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(peopleService, times(1)).updatePatient(any(UpdatePatientRequest.class));
    }

    @Test
    void shouldReturnDoctorWhenFound() {
        Long doctorId = 1L;
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        when(peopleService.getDoctor(doctorId)).thenReturn(doctor);

        ResponseEntity<Doctor> response = peopleController.getDoctor(doctorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(doctor, response.getBody());
        verify(peopleService, times(1)).getDoctor(doctorId);
    }

    @Test
    void shouldUpdateDoctorSuccessfully() {
        Long doctorId = 1L;
        DoctorRequest request = new DoctorRequest();
        request.setFullName("Dr. Smith");
        request.setCpf("12345678900");
        request.setCrm("CRM12345");
        request.setSpecialty("Cardiology");

        doNothing().when(peopleService).updateDoctor(any(UpdateDoctorRequest.class));

        ResponseEntity<Void> response = peopleController.updateDoctor(doctorId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(peopleService, times(1)).updateDoctor(any(UpdateDoctorRequest.class));
    }

    @Test
    void shouldReturnTrueWhenCpfExists() {
        String cpf = "12345678900";
        when(peopleService.checkCpfExists(cpf)).thenReturn(true);

        ResponseEntity<Boolean> response = peopleController.checkCpfExists(cpf);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo");
        assertTrue(response.getBody());
        verify(peopleService, times(1)).checkCpfExists(cpf);
    }

    @Test
    void shouldReturnTrueWhenDoctorExists() {
        Long doctorId = 1L;
        when(peopleService.getDoctor(doctorId)).thenReturn(new Doctor());

        ResponseEntity<Boolean> response = peopleController.doctorExists(doctorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo");
        assertTrue(response.getBody());
        verify(peopleService, times(1)).getDoctor(doctorId);
    }

    @Test
    void shouldReturnFalseWhenDoctorDoesNotExist() {
        Long doctorId = 1L;
        when(peopleService.getDoctor(doctorId)).thenThrow(new RuntimeException());

        ResponseEntity<Boolean> response = peopleController.doctorExists(doctorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo");
        assertFalse(response.getBody());
        verify(peopleService, times(1)).getDoctor(doctorId);
    }
}