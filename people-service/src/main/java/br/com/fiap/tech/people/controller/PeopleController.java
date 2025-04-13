package br.com.fiap.tech.people.controller;

import br.com.fiap.tech.people.domain.*;
import br.com.fiap.tech.people.dto.*;
import br.com.fiap.tech.people.service.PeopleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/people")
@RequiredArgsConstructor
@Tag(name = "People Management", description = "Endpoints for managing patients, doctors, and administrators")
public class PeopleController {

    private final PeopleService peopleService;

    @Operation(
        summary = "Get patient by ID",
        description = "Retrieves patient information by their ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patient found"),
        @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/patients/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable Long id) {
        return ResponseEntity.ok(peopleService.getPatient(id));
    }

    @Operation(
        summary = "Update patient",
        description = "Updates patient information. Required fields: fullName, cpf. Optional fields: nationalHealthCard, birthDate, phoneNumber, address, city, state, zipCode"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patient updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or missing required fields"),
        @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @PutMapping("/patients/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long id,
            @RequestBody PatientRequest request
    ) {
        return ResponseEntity.ok(peopleService.updatePatient(id, request));
    }

    @Operation(
        summary = "Get doctor by ID",
        description = "Retrieves doctor information by their ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Doctor found"),
        @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    @GetMapping("/doctors/{id}")
    public ResponseEntity<Doctor> getDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(peopleService.getDoctor(id));
    }

    @Operation(
        summary = "Update doctor",
        description = "Updates doctor information. Required fields: fullName, cpf, crm, specialty. Optional fields: phoneNumber"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Doctor updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or missing required fields"),
        @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    @PutMapping("/doctors/{id}")
    public ResponseEntity<Doctor> updateDoctor(
            @PathVariable Long id,
            @RequestBody DoctorRequest request
    ) {
        return ResponseEntity.ok(peopleService.updateDoctor(id, request));
    }

    @Operation(
        summary = "Get administrator by ID",
        description = "Retrieves administrator information by their ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Administrator found"),
        @ApiResponse(responseCode = "404", description = "Administrator not found")
    })
    @GetMapping("/administrators/{id}")
    public ResponseEntity<Administrator> getAdministrator(@PathVariable Long id) {
        return ResponseEntity.ok(peopleService.getAdministrator(id));
    }

    @Operation(
        summary = "Update administrator",
        description = "Updates administrator information. Required fields: fullName, cpf. Optional fields: phoneNumber"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Administrator updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or missing required fields"),
        @ApiResponse(responseCode = "404", description = "Administrator not found")
    })
    @PutMapping("/administrators/{id}")
    public ResponseEntity<Administrator> updateAdministrator(
            @PathVariable Long id,
            @RequestBody AdministratorRequest request
    ) {
        return ResponseEntity.ok(peopleService.updateAdministrator(id, request));
    }
}
