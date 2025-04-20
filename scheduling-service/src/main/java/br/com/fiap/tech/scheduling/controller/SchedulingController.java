package br.com.fiap.tech.scheduling.controller;

import br.com.fiap.tech.scheduling.domain.*;
import br.com.fiap.tech.scheduling.dto.*;
import br.com.fiap.tech.scheduling.service.SchedulingService;
import br.com.fiap.tech.scheduling.service.PriorityService;
import br.com.fiap.tech.scheduling.service.AppointmentPriorityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/scheduling")
@RequiredArgsConstructor
@Tag(name = "Scheduling", description = "Endpoints for managing appointments and referrals")
public class SchedulingController {

    private final SchedulingService schedulingService;
    private final PriorityService priorityService;
    private final AppointmentPriorityService appointmentPriorityService;

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

    @Operation(
        summary = "Get prioritized referral queue",
        description = "Retrieves pending referrals ordered by priority with calculated priority score"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Referrals retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/referrals/queue")
    public ResponseEntity<List<ReferralWithPriorityDTO>> getPrioritizedReferrals() {
        return ResponseEntity.ok(priorityService.getPrioritizedReferrals());
    }

    @Operation(
        summary = "Get prioritized referrals by type",
        description = "Retrieves pending referrals of a specific type ordered by priority"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Referrals retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid referral type"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/referrals/queue/by-type/{referralType}")
    public ResponseEntity<List<ReferralWithPriorityDTO>> getPrioritizedReferralsByType(
            @PathVariable ReferralType referralType
    ) {
        return ResponseEntity.ok(priorityService.getPrioritizedReferralsByType(referralType));
    }

    @Hidden
    @Operation(
        summary = "Get next highest priority referral",
        description = "Retrieves the next referral with highest priority score. For internal use by system operators processing the priority queue."
    )
    @ApiResponse(responseCode = "200", description = "Highest priority referral retrieved")
    @GetMapping("/referrals/queue/next")
    public ResponseEntity<ReferralWithPriorityDTO> getNextHighestPriorityReferral() {
        ReferralWithPriorityDTO nextReferral = priorityService.getNextHighestPriorityReferral();
        if (nextReferral == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(nextReferral);
    }

    @Operation(
        summary = "Cancel appointment",
        description = "Cancels an existing appointment and releases capacity in the healthcare facility. " +
                    "Unlike the status update endpoint, this performs additional validations specific to cancellation, " +
                    "updates facility capacity, and reverts referral status if applicable."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Appointment cancelled successfully"),
        @ApiResponse(responseCode = "404", description = "Appointment not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request or appointment already cancelled")
    })
    @PatchMapping("/appointments/{id}/cancel")
    public ResponseEntity<Appointment> cancelAppointment(
            @PathVariable Long id,
            @RequestParam(required = false) String cancellationReason
    ) {
        return ResponseEntity.ok(schedulingService.cancelAppointment(id, cancellationReason));
    }

    @Operation(
        summary = "Get prioritized appointments queue",
        description = "Retrieves scheduled appointments ordered by priority with calculated priority score"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Appointments retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/appointments/queue")
    public ResponseEntity<List<AppointmentWithPriorityDTO>> getPrioritizedAppointments() {
        return ResponseEntity.ok(appointmentPriorityService.getPrioritizedAppointments());
    }

    @Operation(
        summary = "Get prioritized appointments by date",
        description = "Retrieves scheduled appointments for a specific date ordered by priority"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Appointments retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid date format"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/appointments/queue/by-date")
    public ResponseEntity<List<AppointmentWithPriorityDTO>> getPrioritizedAppointmentsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(appointmentPriorityService.getPrioritizedAppointmentsByDate(date));
    }

    @Hidden
    @Operation(
        summary = "Get next highest priority appointment",
        description = "Retrieves the next appointment with highest priority score. For internal use by system operators processing the priority queue."
    )
    @ApiResponse(responseCode = "200", description = "Highest priority appointment retrieved")
    @GetMapping("/appointments/queue/next")
    public ResponseEntity<AppointmentWithPriorityDTO> getNextHighestPriorityAppointment() {
        AppointmentWithPriorityDTO nextAppointment = appointmentPriorityService.getNextHighestPriorityAppointment();
        if (nextAppointment == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(nextAppointment);
    }
}