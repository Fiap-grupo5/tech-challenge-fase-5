package br.com.fiap.tech.facility.dto;

import br.com.fiap.tech.facility.domain.DayOfWeek;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoctorScheduleRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldPassValidationWhenFieldsAreValid() {
        DoctorScheduleRequest request = new DoctorScheduleRequest();
        request.setDayOfWeek(DayOfWeek.MONDAY);
        request.setStartTime(LocalTime.of(9, 0));
        request.setEndTime(LocalTime.of(12, 0));
        request.setSecondPeriodStart(LocalTime.of(14, 0));
        request.setSecondPeriodEnd(LocalTime.of(18, 0));
        request.setDoctorId(1L);
        request.setFacilityId(1L);

        Set<ConstraintViolation<DoctorScheduleRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), "Não deve haver violações de validação");
    }

    @Test
    void shouldFailValidationWhenDayOfWeekIsNull() {
        DoctorScheduleRequest request = new DoctorScheduleRequest();
        request.setDayOfWeek(null);
        request.setStartTime(LocalTime.of(9, 0));
        request.setEndTime(LocalTime.of(12, 0));
        request.setDoctorId(1L);
        request.setFacilityId(1L);

        Set<ConstraintViolation<DoctorScheduleRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("O dia da semana é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenStartTimeIsNull() {
        DoctorScheduleRequest request = new DoctorScheduleRequest();
        request.setDayOfWeek(DayOfWeek.MONDAY);
        request.setStartTime(null);
        request.setEndTime(LocalTime.of(12, 0));
        request.setDoctorId(1L);
        request.setFacilityId(1L);

        Set<ConstraintViolation<DoctorScheduleRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("O horário de início é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenEndTimeIsNull() {
        DoctorScheduleRequest request = new DoctorScheduleRequest();
        request.setDayOfWeek(DayOfWeek.MONDAY);
        request.setStartTime(LocalTime.of(9, 0));
        request.setEndTime(null);
        request.setDoctorId(1L);
        request.setFacilityId(1L);

        Set<ConstraintViolation<DoctorScheduleRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("O horário de término é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenDoctorIdIsNull() {
        DoctorScheduleRequest request = new DoctorScheduleRequest();
        request.setDayOfWeek(DayOfWeek.MONDAY);
        request.setStartTime(LocalTime.of(9, 0));
        request.setEndTime(LocalTime.of(12, 0));
        request.setDoctorId(null);
        request.setFacilityId(1L);

        Set<ConstraintViolation<DoctorScheduleRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("O ID do médico é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenDoctorIdIsNegative() {
        DoctorScheduleRequest request = new DoctorScheduleRequest();
        request.setDayOfWeek(DayOfWeek.MONDAY);
        request.setStartTime(LocalTime.of(9, 0));
        request.setEndTime(LocalTime.of(12, 0));
        request.setDoctorId(-1L);
        request.setFacilityId(1L);

        Set<ConstraintViolation<DoctorScheduleRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("O ID do médico deve ser positivo", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenFacilityIdIsNull() {
        DoctorScheduleRequest request = new DoctorScheduleRequest();
        request.setDayOfWeek(DayOfWeek.MONDAY);
        request.setStartTime(LocalTime.of(9, 0));
        request.setEndTime(LocalTime.of(12, 0));
        request.setDoctorId(1L);
        request.setFacilityId(null);

        Set<ConstraintViolation<DoctorScheduleRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("O ID da unidade de saúde é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenFacilityIdIsNegative() {
        DoctorScheduleRequest request = new DoctorScheduleRequest();
        request.setDayOfWeek(DayOfWeek.MONDAY);
        request.setStartTime(LocalTime.of(9, 0));
        request.setEndTime(LocalTime.of(12, 0));
        request.setDoctorId(1L);
        request.setFacilityId(-1L);

        Set<ConstraintViolation<DoctorScheduleRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("O ID da unidade de saúde deve ser positivo", violations.iterator().next().getMessage());
    }
}
