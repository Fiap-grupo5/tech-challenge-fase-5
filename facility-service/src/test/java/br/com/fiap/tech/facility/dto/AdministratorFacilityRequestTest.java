package br.com.fiap.tech.facility.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdministratorFacilityRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldPassValidationWhenFieldsAreValid() {
        AdministratorFacilityRequest request = new AdministratorFacilityRequest();
        request.setAdministratorId(1L);
        request.setHealthcareFacilityId(1L);

        Set<ConstraintViolation<AdministratorFacilityRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), "Não deve haver violações de validação");
    }

    @Test
    void shouldFailValidationWhenAdministratorIdIsNull() {
        AdministratorFacilityRequest request = new AdministratorFacilityRequest();
        request.setAdministratorId(null);
        request.setHealthcareFacilityId(1L);

        Set<ConstraintViolation<AdministratorFacilityRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("O ID do administrador é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenAdministratorIdIsNegative() {
        AdministratorFacilityRequest request = new AdministratorFacilityRequest();
        request.setAdministratorId(-1L);
        request.setHealthcareFacilityId(1L);

        Set<ConstraintViolation<AdministratorFacilityRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("O ID do administrador deve ser positivo", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenHealthcareFacilityIdIsNull() {
        AdministratorFacilityRequest request = new AdministratorFacilityRequest();
        request.setAdministratorId(1L);
        request.setHealthcareFacilityId(null);

        Set<ConstraintViolation<AdministratorFacilityRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("O ID da unidade de saúde é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenHealthcareFacilityIdIsNegative() {
        AdministratorFacilityRequest request = new AdministratorFacilityRequest();
        request.setAdministratorId(1L);
        request.setHealthcareFacilityId(-1L);

        Set<ConstraintViolation<AdministratorFacilityRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("O ID da unidade de saúde deve ser positivo", violations.iterator().next().getMessage());
    }
}