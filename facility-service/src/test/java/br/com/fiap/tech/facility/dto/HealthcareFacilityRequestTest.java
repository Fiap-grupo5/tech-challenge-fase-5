package br.com.fiap.tech.facility.dto;

import br.com.fiap.tech.facility.domain.FacilityType;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HealthcareFacilityRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldPassValidationWhenFieldsAreValid() {
        HealthcareFacilityRequest request = new HealthcareFacilityRequest();
        request.setName("Hospital São Luiz");
        request.setFacilityType(FacilityType.HOSPITAL);
        request.setCnpj("12345678901234");
        request.setPhoneNumber("(11) 98765-4321");
        request.setAddress("Av. Paulista, 1000");
        request.setCity("São Paulo");
        request.setState("SP");
        request.setZipCode("01310100");
        request.setLatitude(-23.5505);
        request.setLongitude(-46.6333);
        request.setMaxDailyCapacity(100);

        Set<ConstraintViolation<HealthcareFacilityRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), "Não deve haver violações de validação");
    }

    @Test
    void shouldFailValidationWhenNameIsBlank() {
        HealthcareFacilityRequest request = new HealthcareFacilityRequest();
        request.setName("");
        request.setFacilityType(FacilityType.HOSPITAL);
        request.setCnpj("12345678901234");
        request.setPhoneNumber("(11) 98765-4321");
        request.setAddress("Av. Paulista, 1000");
        request.setCity("São Paulo");
        request.setState("SP");
        request.setZipCode("01310100");
        request.setLatitude(-23.5505);
        request.setLongitude(-46.6333);
        request.setMaxDailyCapacity(100);

        Set<ConstraintViolation<HealthcareFacilityRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("O nome da unidade de saúde é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenCnpjIsInvalid() {
        HealthcareFacilityRequest request = new HealthcareFacilityRequest();
        request.setName("Hospital São Luiz");
        request.setFacilityType(FacilityType.HOSPITAL);
        request.setCnpj("123");
        request.setPhoneNumber("(11) 98765-4321");
        request.setAddress("Av. Paulista, 1000");
        request.setCity("São Paulo");
        request.setState("SP");
        request.setZipCode("01310100");
        request.setLatitude(-23.5505);
        request.setLongitude(-46.6333);
        request.setMaxDailyCapacity(100);

        Set<ConstraintViolation<HealthcareFacilityRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("CNPJ deve conter exatamente 14 dígitos numéricos", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenLatitudeIsOutOfRange() {
        HealthcareFacilityRequest request = new HealthcareFacilityRequest();
        request.setName("Hospital São Luiz");
        request.setFacilityType(FacilityType.HOSPITAL);
        request.setCnpj("12345678901234");
        request.setPhoneNumber("(11) 98765-4321");
        request.setAddress("Av. Paulista, 1000");
        request.setCity("São Paulo");
        request.setState("SP");
        request.setZipCode("01310100");
        request.setLatitude(-91.0);
        request.setLongitude(-46.6333);
        request.setMaxDailyCapacity(100);

        Set<ConstraintViolation<HealthcareFacilityRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Latitude mínima: -90.0", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenMaxDailyCapacityIsZero() {
        HealthcareFacilityRequest request = new HealthcareFacilityRequest();
        request.setName("Hospital São Luiz");
        request.setFacilityType(FacilityType.HOSPITAL);
        request.setCnpj("12345678901234");
        request.setPhoneNumber("(11) 98765-4321");
        request.setAddress("Av. Paulista, 1000");
        request.setCity("São Paulo");
        request.setState("SP");
        request.setZipCode("01310100");
        request.setLatitude(-23.5505);
        request.setLongitude(-46.6333);
        request.setMaxDailyCapacity(0);

        Set<ConstraintViolation<HealthcareFacilityRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("A capacidade máxima diária deve ser maior que zero", violations.iterator().next().getMessage());
    }
}