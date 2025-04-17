package br.com.fiap.tech.facility.validation;

import br.com.fiap.tech.facility.dto.DoctorScheduleRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeOrderValidator.Validator.class)
public @interface TimeOrderValidator {
    String message() default "O horário de término deve ser posterior ao horário de início";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    class Validator implements ConstraintValidator<TimeOrderValidator, DoctorScheduleRequest> {
        @Override
        public boolean isValid(DoctorScheduleRequest request, ConstraintValidatorContext context) {
            if (request.getStartTime() == null || request.getEndTime() == null) {
                return true; // As validações @NotNull já tratarão este caso
            }
            
            return request.getEndTime().isAfter(request.getStartTime());
        }
    }
} 