package br.com.fiap.tech.facility.controller;

import br.com.fiap.tech.facility.domain.*;
import br.com.fiap.tech.facility.dto.*;
import br.com.fiap.tech.facility.service.FacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/facilities")
@RequiredArgsConstructor
@Tag(name = "Facility Management", description = "Endpoints for managing healthcare facilities and doctor schedules")
public class FacilityController {

    private final FacilityService facilityService;

    @Operation(
        summary = "Create healthcare facility",
        description = "Creates a new healthcare facility with capacity tracking"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Facility created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Facility already exists")
    })
    @PostMapping
    public ResponseEntity<HealthcareFacility> createFacility(@RequestBody HealthcareFacilityRequest request) {
        return ResponseEntity.ok(facilityService.createFacility(request));
    }

    @Operation(
        summary = "Create doctor schedule",
        description = "Creates a new schedule for a doctor at a facility"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Schedule created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Doctor or facility not found"),
        @ApiResponse(responseCode = "409", description = "Schedule conflict")
    })
    @PostMapping("/schedules")
    public ResponseEntity<DoctorSchedule> createDoctorSchedule(@RequestBody DoctorScheduleRequest request) {
        return ResponseEntity.ok(facilityService.createDoctorSchedule(request));
    }

    @Operation(
        summary = "Assign administrator to facility",
        description = "Associates an administrator with a healthcare facility"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Administrator assigned successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Administrator or facility not found")
    })
    @PostMapping("/administrators")
    public ResponseEntity<AdministratorFacility> assignAdministratorToFacility(
            @RequestBody AdministratorFacilityRequest request
    ) {
        return ResponseEntity.ok(facilityService.assignAdministratorToFacility(request));
    }

    @Operation(
        summary = "Get facility by ID",
        description = "Retrieves healthcare facility information by its ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Facility found"),
        @ApiResponse(responseCode = "404", description = "Facility not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<HealthcareFacility> getFacility(@PathVariable Long id) {
        return ResponseEntity.ok(facilityService.getFacility(id));
    }

    @Operation(
        summary = "Get available facilities",
        description = "Retrieves a list of facilities that have available capacity"
    )
    @ApiResponse(responseCode = "200", description = "List of available facilities retrieved successfully")
    @GetMapping("/available")
    public ResponseEntity<List<HealthcareFacility>> getAvailableFacilities() {
        return ResponseEntity.ok(facilityService.getAvailableFacilities());
    }

    @Operation(
        summary = "Get doctor schedules",
        description = "Retrieves all schedules for a specific doctor"
    )
    @ApiResponse(responseCode = "200", description = "List of schedules retrieved successfully")
    @GetMapping("/schedules/doctor/{doctorId}")
    public ResponseEntity<List<DoctorSchedule>> getDoctorSchedules(@PathVariable Long doctorId) {
        return ResponseEntity.ok(facilityService.getDoctorSchedules(doctorId));
    }

    @Operation(
        summary = "Get facility schedules",
        description = "Retrieves all doctor schedules for a specific facility"
    )
    @ApiResponse(responseCode = "200", description = "List of schedules retrieved successfully")
    @GetMapping("/{facilityId}/schedules")
    public ResponseEntity<List<DoctorSchedule>> getFacilitySchedules(@PathVariable Long facilityId) {
        return ResponseEntity.ok(facilityService.getFacilitySchedules(facilityId));
    }

    @Operation(
        summary = "Get administrator facilities",
        description = "Retrieves all facilities managed by a specific administrator"
    )
    @ApiResponse(responseCode = "200", description = "List of facilities retrieved successfully")
    @GetMapping("/administrators/{administratorId}")
    public ResponseEntity<List<AdministratorFacility>> getAdministratorFacilities(
            @PathVariable Long administratorId
    ) {
        return ResponseEntity.ok(facilityService.getAdministratorFacilities(administratorId));
    }

    @Operation(
        summary = "Get facility administrators",
        description = "Retrieves all administrators for a specific facility"
    )
    @ApiResponse(responseCode = "200", description = "List of administrators retrieved successfully")
    @GetMapping("/{facilityId}/administrators")
    public ResponseEntity<List<AdministratorFacility>> getFacilityAdministrators(
            @PathVariable Long facilityId
    ) {
        return ResponseEntity.ok(facilityService.getFacilityAdministrators(facilityId));
    }

    @Operation(
        summary = "Find nearby facilities",
        description = "Finds healthcare facilities within a specified radius from given coordinates"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of nearby facilities retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @GetMapping("/nearby")
    public ResponseEntity<List<NearbyFacilitiesResponse>> findNearbyFacilities(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "10.0") Double radiusInKm,
            @RequestParam(defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(facilityService.findNearbyFacilities(latitude, longitude, radiusInKm, limit));
    }
}
