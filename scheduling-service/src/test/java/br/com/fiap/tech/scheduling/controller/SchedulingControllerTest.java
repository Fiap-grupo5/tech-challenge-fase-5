package br.com.fiap.tech.scheduling.controller;

import br.com.fiap.tech.scheduling.domain.Appointment;
import br.com.fiap.tech.scheduling.domain.Referral;
import br.com.fiap.tech.scheduling.dto.AppointmentRequest;
import br.com.fiap.tech.scheduling.dto.ReferralRequest;
import br.com.fiap.tech.scheduling.service.SchedulingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SchedulingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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
    void createAppointment_Success() throws Exception {
        when(schedulingService.createAppointment(any(AppointmentRequest.class)))
                .thenReturn(appointment);

        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    void getAppointment_Success() throws Exception {
        when(schedulingService.getAppointment(1L)).thenReturn(appointment);

        mockMvc.perform(get("/api/v1/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    void getPatientAppointments_Success() throws Exception {
        when(schedulingService.getPatientAppointments(1L))
                .thenReturn(Arrays.asList(appointment));

        mockMvc.perform(get("/api/v1/appointments/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientId").value(1))
                .andExpect(jsonPath("$[0].status").value("SCHEDULED"));
    }

    @Test
    void getDoctorAppointments_Success() throws Exception {
        when(schedulingService.getDoctorAppointments(1L))
                .thenReturn(Arrays.asList(appointment));

        mockMvc.perform(get("/api/v1/appointments/doctor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorId").value(1))
                .andExpect(jsonPath("$[0].status").value("SCHEDULED"));
    }

    @Test
    void createReferral_Success() throws Exception {
        when(schedulingService.createReferral(any(ReferralRequest.class)))
                .thenReturn(referral);

        mockMvc.perform(post("/api/v1/referrals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(referralRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void getReferral_Success() throws Exception {
        when(schedulingService.getReferral(1L)).thenReturn(referral);

        mockMvc.perform(get("/api/v1/referrals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void getPatientReferrals_Success() throws Exception {
        when(schedulingService.getPatientReferrals(1L))
                .thenReturn(Arrays.asList(referral));

        mockMvc.perform(get("/api/v1/referrals/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientId").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void getDoctorReferrals_Success() throws Exception {
        when(schedulingService.getDoctorReferrals(2L))
                .thenReturn(Arrays.asList(referral));

        mockMvc.perform(get("/api/v1/referrals/doctor/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].specialistDoctorId").value(2))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }
}
