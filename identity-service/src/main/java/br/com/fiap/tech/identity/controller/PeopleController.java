package br.com.fiap.tech.identity.controller;

import br.com.fiap.tech.identity.service.PeopleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/people")
@RequiredArgsConstructor
@Tag(name = "People", description = "People API")
public class PeopleController {

    private final PeopleService peopleService;

    @Hidden
    @Operation(
        summary = "Check if CPF exists",
        description = "Verifies if the provided CPF is already registered in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verification completed successfully")
    })
    @GetMapping("/check-cpf")
    public ResponseEntity<Boolean> checkCpfExists(@RequestParam("cpf") String cpf) {
        log.info("Checking if CPF exists: {}", cpf);
        boolean exists = peopleService.checkCpfExists(cpf);
        log.info("CPF {} exists: {}", cpf, exists);
        return ResponseEntity.ok(exists);
    }

    @Hidden
    @Operation(
        summary = "Check if CRM exists",
        description = "Verifies if the provided CRM is already registered in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verification completed successfully")
    })
    @GetMapping("/check-crm")
    public ResponseEntity<Boolean> checkCrmExists(@RequestParam("crm") String crm) {
        log.info("Checking if CRM exists: {}", crm);
        boolean exists = peopleService.checkCrmExists(crm);
        log.info("CRM {} exists: {}", crm, exists);
        return ResponseEntity.ok(exists);
    }
} 