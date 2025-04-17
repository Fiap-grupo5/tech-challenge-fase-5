package br.com.fiap.tech.facility.controller;

import br.com.fiap.tech.facility.domain.*;
import br.com.fiap.tech.facility.dto.*;
import br.com.fiap.tech.facility.exception.ApiErrorSchema;
import br.com.fiap.tech.facility.service.FacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/facilities")
@RequiredArgsConstructor
@Validated
@Tag(name = "Facility Management", description = "Endpoints for managing healthcare facilities and doctor schedules")
public class FacilityController {

    private final FacilityService facilityService;

    @Operation(
        summary = "Create healthcare facility",
        description = "Creates a new healthcare facility with capacity tracking"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Facility created successfully"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorSchema.class)
            )
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "Facility already exists",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorSchema.class)
            )
        )
    })
    @PostMapping
    public ResponseEntity<HealthcareFacility> createFacility(@Valid @RequestBody HealthcareFacilityRequest request) {
        HealthcareFacility facility = facilityService.createFacility(request);
        return ResponseEntity
                .created(URI.create("/api/v1/facilities/" + facility.getId()))
                .body(facility);
    }

    @Operation(
        summary = "Create doctor schedule",
        description = "Creates a new schedule for a doctor at a facility"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Schedule created successfully"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorSchema.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Doctor or facility not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorSchema.class)
            )
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "Schedule conflict",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorSchema.class)
            )
        )
    })
    @PostMapping("/schedules")
    public ResponseEntity<DoctorSchedule> createDoctorSchedule(@Valid @RequestBody DoctorScheduleRequest request) {
        DoctorSchedule schedule = facilityService.createDoctorSchedule(request);
        return ResponseEntity
                .created(URI.create("/api/v1/facilities/schedules/" + schedule.getId()))
                .body(schedule);
    }

    @Operation(
        summary = "Assign administrator to facility",
        description = "Associates an administrator with a healthcare facility"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Administrator assigned successfully"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorSchema.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Administrator or facility not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorSchema.class)
            )
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "Administrator already assigned to this facility",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorSchema.class)
            )
        )
    })
    @PostMapping("/administrators")
    public ResponseEntity<AdministratorFacility> assignAdministratorToFacility(
            @Valid @RequestBody AdministratorFacilityRequest request
    ) {
        AdministratorFacility adminFacility = facilityService.assignAdministratorToFacility(request);
        return ResponseEntity
                .created(URI.create("/api/v1/facilities/administrators/" + adminFacility.getId()))
                .body(adminFacility);
    }

    @Operation(
        summary = "Get facility by ID",
        description = "Retrieves healthcare facility information by its ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Facility found"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Facility not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorSchema.class)
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<HealthcareFacility> getFacility(@PathVariable @Positive(message = "O ID deve ser positivo") Long id) {
        return ResponseEntity.ok(facilityService.getFacility(id));
    }

    @Operation(
        summary = "Get available facilities",
        description = "Retrieves a list of facilities that have available capacity"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "List of available facilities retrieved successfully"
    )
    @GetMapping("/available")
    public ResponseEntity<List<HealthcareFacility>> getAvailableFacilities() {
        return ResponseEntity.ok(facilityService.getAvailableFacilities());
    }

    @Operation(
        summary = "Get doctor schedules",
        description = "Retrieves all schedules for a specific doctor"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "List of schedules retrieved successfully"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid doctor ID",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorSchema.class)
            )
        )
    })
    @GetMapping("/schedules/doctor/{doctorId}")
    public ResponseEntity<List<DoctorSchedule>> getDoctorSchedules(
            @PathVariable @Positive(message = "O ID do médico deve ser positivo") Long doctorId
    ) {
        return ResponseEntity.ok(facilityService.getDoctorSchedules(doctorId));
    }

    @Operation(
        summary = "Get facility schedules",
        description = "Retrieves all doctor schedules for a specific facility"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "List of schedules retrieved successfully"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid facility ID",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorSchema.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Facility not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorSchema.class)
            )
        )
    })
    @GetMapping("/{facilityId}/schedules")
    public ResponseEntity<List<DoctorSchedule>> getFacilitySchedules(
            @PathVariable @Positive(message = "O ID da unidade de saúde deve ser positivo") Long facilityId
    ) {
        return ResponseEntity.ok(facilityService.getFacilitySchedules(facilityId));
    }

    @Operation(
        summary = "Get administrator facilities",
        description = "Retrieves all facilities managed by a specific administrator"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "List of facilities retrieved successfully"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid administrator ID",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorSchema.class)
            )
        )
    })
    @GetMapping("/administrators/{administratorId}")
    public ResponseEntity<List<AdministratorFacility>> getAdministratorFacilities(
            @PathVariable @Positive(message = "O ID do administrador deve ser positivo") Long administratorId
    ) {
        return ResponseEntity.ok(facilityService.getAdministratorFacilities(administratorId));
    }

    @Operation(
        summary = "Get facility administrators",
        description = "Retrieves all administrators for a specific facility"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "List of administrators retrieved successfully"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid facility ID",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorSchema.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Facility not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorSchema.class)
            )
        )
    })
    @GetMapping("/{facilityId}/administrators")
    public ResponseEntity<List<AdministratorFacility>> getFacilityAdministrators(
            @PathVariable @Positive(message = "O ID da unidade de saúde deve ser positivo") Long facilityId
    ) {
        return ResponseEntity.ok(facilityService.getFacilityAdministrators(facilityId));
    }

    @Operation(
        summary = "Find nearby facilities",
        description = "Finds healthcare facilities within a specified radius from given coordinates"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "List of nearby facilities retrieved successfully"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input parameters",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorSchema.class)
            )
        )
    })
    @GetMapping("/nearby")
    public ResponseEntity<List<NearbyFacilitiesResponse>> findNearbyFacilities(
            @RequestParam @DecimalMin(value = "-90.0", message = "Latitude mínima: -90.0") 
                        @DecimalMax(value = "90.0", message = "Latitude máxima: 90.0") Double latitude,
            @RequestParam @DecimalMin(value = "-180.0", message = "Longitude mínima: -180.0") 
                        @DecimalMax(value = "180.0", message = "Longitude máxima: 180.0") Double longitude,
            @RequestParam(defaultValue = "10.0") @Positive(message = "O raio deve ser positivo") Double radiusInKm,
            @RequestParam(defaultValue = "10") @Positive(message = "O limite deve ser positivo") Integer limit) {
        return ResponseEntity.ok(facilityService.findNearbyFacilities(latitude, longitude, radiusInKm, limit));
    }
}
