package br.com.fiap.tech.identity.controller;

import br.com.fiap.tech.identity.domain.User;
import br.com.fiap.tech.identity.domain.UserType;
import br.com.fiap.tech.identity.dto.AdministratorDTO;
import br.com.fiap.tech.identity.dto.AuthenticationRequest;
import br.com.fiap.tech.identity.dto.AuthenticationResponse;
import br.com.fiap.tech.identity.dto.ErrorResponse;
import br.com.fiap.tech.identity.dto.RegisterRequest;
import br.com.fiap.tech.identity.service.AuthenticationService;
import br.com.fiap.tech.identity.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;

    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account with the provided credentials and role. " +
                     "Required fields for all types: username, password, userType, fullName, cpf, email. " +
                     "Additional required fields for doctors: crm, specialty. " +
                     "Additional required fields for patients: nationalHealthCard. " +
                     "Optional fields for all types: phoneNumber, birthDate, address, city, state, zipCode."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User successfully registered"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or missing required fields"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        try {
            return ResponseEntity.ok(authenticationService.register(request));
        } catch (IllegalArgumentException e) {
            log.error("Error registering user: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (RuntimeException e) {
            log.error("Error registering user: {}", e.getMessage());
            if (e.getMessage().contains("already exists")) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new ErrorResponse(e.getMessage()));
            }
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An unexpected error occurred"));
        }
    }

    @Operation(
        summary = "Authenticate user",
        description = "Authenticates a user and returns a JWT token"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        try {
            return ResponseEntity.ok(authenticationService.authenticate(request));
        } catch (Exception e) {
            log.error("Error authenticating user: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid credentials"));
        }
    }

    @Operation(
        summary = "Check if administrator exists",
        description = "Verifies if an administrator with the given ID exists"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verification completed successfully")
    })
    @GetMapping("/administrators/{id}/exists")
    public ResponseEntity<Boolean> administratorExists(@PathVariable Long id) {
        try {
            boolean exists = authenticationService.checkAdministratorExists(id);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
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
    public ResponseEntity<AdministratorDTO> getAdministrator(@PathVariable Long id) {
        User user = authenticationService.getUserById(id);
        if (user != null && UserType.ADMINISTRATOR.equals(user.getUserType())) {
            AdministratorDTO admin = AdministratorDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
            return ResponseEntity.ok(admin);
        }
        return ResponseEntity.notFound().build();
    }
}
