package br.com.fiap.tech.people.controller;

import br.com.fiap.tech.people.domain.Doctor;
import br.com.fiap.tech.people.domain.Patient;
import br.com.fiap.tech.people.dto.DoctorRequest;
import br.com.fiap.tech.people.dto.PatientRequest;
import br.com.fiap.tech.people.service.PeopleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PeopleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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
    void createPatient_Success() throws Exception {
        when(peopleService.createPatient(any(PatientRequest.class)))
                .thenReturn(patient);

        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.cpf").value("12345678901"));
    }

    @Test
    void getPatient_Success() throws Exception {
        when(peopleService.getPatient(1L)).thenReturn(patient);

        mockMvc.perform(get("/api/v1/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.cpf").value("12345678901"));
    }

    @Test
    void getAllPatients_Success() throws Exception {
        when(peopleService.getAllPatients()).thenReturn(Arrays.asList(patient));

        mockMvc.perform(get("/api/v1/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].cpf").value("12345678901"));
    }

    @Test
    void createDoctor_Success() throws Exception {
        when(peopleService.createDoctor(any(DoctorRequest.class)))
                .thenReturn(doctor);

        mockMvc.perform(post("/api/v1/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.crm").value("CRM123456"));
    }

    @Test
    void getDoctor_Success() throws Exception {
        when(peopleService.getDoctor(1L)).thenReturn(doctor);

        mockMvc.perform(get("/api/v1/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.crm").value("CRM123456"));
    }

    @Test
    void getAllDoctors_Success() throws Exception {
        when(peopleService.getAllDoctors()).thenReturn(Arrays.asList(doctor));

        mockMvc.perform(get("/api/v1/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jane"))
                .andExpect(jsonPath("$[0].crm").value("CRM123456"));
    }

    @Test
    void getDoctorsBySpecialty_Success() throws Exception {
        when(peopleService.getDoctorsBySpecialty("Cardiology"))
                .thenReturn(Arrays.asList(doctor));

        mockMvc.perform(get("/api/v1/doctors/specialty/Cardiology"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jane"))
                .andExpect(jsonPath("$[0].specialty").value("Cardiology"));
    }
}
