package br.com.fiap.tech.scheduling.controller;

import br.com.fiap.tech.scheduling.domain.*;
import br.com.fiap.tech.scheduling.dto.*;
import br.com.fiap.tech.scheduling.service.SchedulingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/scheduling")
@RequiredArgsConstructor
@Tag(name = "Scheduling", description = "Endpoints for managing appointments and referrals")
public class SchedulingController {

    private final SchedulingService schedulingService;

    @Operation(
        summary = "Criar agendamento",
        description = "Cria um novo agendamento para um paciente com um médico em uma unidade de saúde"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agendamento criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou conflito de agendamento"),
        @ApiResponse(responseCode = "404", description = "Paciente ou médico não encontrado")
    })
    @PostMapping("/appointments")
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(schedulingService.createAppointment(request));
    }

    @Operation(
        summary = "Create referral",
        description = "Creates a new referral for a patient to a specialist"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Referral created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Patient or doctor not found")
    })
    @PostMapping("/referrals")
    public ResponseEntity<Referral> createReferral(@RequestBody ReferralRequest request) {
        return ResponseEntity.ok(schedulingService.createReferral(request));
    }

    @Operation(
        summary = "Update appointment status",
        description = "Updates the status of an existing appointment"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Appointment status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status"),
        @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @PatchMapping("/appointments/{id}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status
    ) {
        return ResponseEntity.ok(schedulingService.updateAppointmentStatus(id, status));
    }

    @Operation(
        summary = "Update referral status",
        description = "Updates the status of an existing referral"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Referral status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status"),
        @ApiResponse(responseCode = "404", description = "Referral not found")
    })
    @PatchMapping("/referrals/{id}/status")
    public ResponseEntity<Referral> updateReferralStatus(
            @PathVariable Long id,
            @RequestParam ReferralStatus status
    ) {
        return ResponseEntity.ok(schedulingService.updateReferralStatus(id, status));
    }

    @Operation(
        summary = "Get patient appointments",
        description = "Retrieves all appointments for a specific patient"
    )
    @ApiResponse(responseCode = "200", description = "List of appointments retrieved successfully")
    @GetMapping("/appointments/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getPatientAppointments(@PathVariable Long patientId) {
        return ResponseEntity.ok(schedulingService.getPatientAppointments(patientId));
    }

    @Operation(
        summary = "Get doctor appointments",
        description = "Retrieves all appointments for a specific doctor"
    )
    @ApiResponse(responseCode = "200", description = "List of appointments retrieved successfully")
    @GetMapping("/appointments/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(@PathVariable Long doctorId) {
        return ResponseEntity.ok(schedulingService.getDoctorAppointments(doctorId));
    }

    @Operation(
        summary = "Get patient referrals",
        description = "Retrieves all referrals for a specific patient"
    )
    @ApiResponse(responseCode = "200", description = "List of referrals retrieved successfully")
    @GetMapping("/referrals/patient/{patientId}")
    public ResponseEntity<List<Referral>> getPatientReferrals(@PathVariable Long patientId) {
        return ResponseEntity.ok(schedulingService.getPatientReferrals(patientId));
    }

    @Operation(
        summary = "Get doctor referrals",
        description = "Retrieves all referrals for a specific doctor"
    )
    @ApiResponse(responseCode = "200", description = "List of referrals retrieved successfully")
    @GetMapping("/referrals/doctor/{doctorId}")
    public ResponseEntity<List<Referral>> getDoctorReferrals(@PathVariable Long doctorId) {
        return ResponseEntity.ok(schedulingService.getDoctorReferrals(doctorId));
    }

    @Operation(
        summary = "Find nearby facilities for referral",
        description = "Finds healthcare facilities near a patient's location for referral purposes"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of nearby facilities retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @GetMapping("/referrals/nearby-facilities")
    public ResponseEntity<List<NearbyFacilityResponse>> findNearbyFacilitiesForReferral(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "10.0") Double radiusInKm,
            @RequestParam(defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(schedulingService.findNearbyFacilitiesForReferral(latitude, longitude, radiusInKm, limit));
    }
}
