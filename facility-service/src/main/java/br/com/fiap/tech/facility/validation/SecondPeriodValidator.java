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
@Constraint(validatedBy = SecondPeriodValidator.Validator.class)
public @interface SecondPeriodValidator {
    String message() default "Configuração inválida do segundo período";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    class Validator implements ConstraintValidator<SecondPeriodValidator, DoctorScheduleRequest> {
        @Override
        public boolean isValid(DoctorScheduleRequest request, ConstraintValidatorContext context) {
            // Se não tiver segundo período, é válido
            if (request.getSecondPeriodStart() == null && request.getSecondPeriodEnd() == null) {
                return true;
            }
            
            // Se tiver apenas um dos horários do segundo período preenchido, é inválido
            if ((request.getSecondPeriodStart() == null && request.getSecondPeriodEnd() != null) ||
                (request.getSecondPeriodStart() != null && request.getSecondPeriodEnd() == null)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "Se informar um horário do segundo período, deve informar ambos")
                        .addConstraintViolation();
                return false;
            }
            
            // Se o segundo período começar antes do primeiro período terminar, é inválido
            if (request.getEndTime() != null && request.getSecondPeriodStart() != null && 
                !request.getSecondPeriodStart().isAfter(request.getEndTime())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "O início do segundo período deve ser posterior ao término do primeiro período")
                        .addConstraintViolation();
                return false;
            }
            
            // Se o fim do segundo período for antes do início do segundo período, é inválido
            if (request.getSecondPeriodStart() != null && request.getSecondPeriodEnd() != null &&
                !request.getSecondPeriodEnd().isAfter(request.getSecondPeriodStart())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "O término do segundo período deve ser posterior ao início do segundo período")
                        .addConstraintViolation();
                return false;
            }
            
            return true;
        }
    }
} 