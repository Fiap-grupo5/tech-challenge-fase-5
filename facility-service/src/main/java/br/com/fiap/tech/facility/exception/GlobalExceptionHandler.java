package br.com.fiap.tech.facility.exception;

import io.micrometer.core.instrument.Counter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Counter validationErrorCounter;
    private final Counter notFoundErrorCounter;
    private final Counter conflictErrorCounter;
    private final Counter serverErrorCounter;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(
            EntityNotFoundException ex, 
            HttpServletRequest request) {
        
        log.error("Recurso não encontrado: {}", ex.getMessage());
        notFoundErrorCounter.increment();
        
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );
        
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        
        log.error("Erro de validação: {}", ex.getMessage());
        validationErrorCounter.increment();
        
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                "Verifique os campos enviados",
                request.getRequestURI()
        );
        
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = error instanceof FieldError ? ((FieldError) error).getField() : error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            apiError.addDetail(fieldName + ": " + errorMessage);
        });
        
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(
            ConstraintViolationException ex,
            HttpServletRequest request) {
        
        log.error("Erro de validação: {}", ex.getMessage());
        validationErrorCounter.increment();
        
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                "Verifique os parâmetros enviados",
                request.getRequestURI()
        );
        
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            apiError.addDetail(violation.getPropertyPath() + ": " + violation.getMessage());
        }
        
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        
        log.error("Erro de formato: {}", ex.getMessage());
        validationErrorCounter.increment();
        
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Formato inválido",
                "O conteúdo enviado não está em um formato válido",
                request.getRequestURI()
        );
        
        if (ex.getCause() != null) {
            apiError.addDetail(ex.getCause().getMessage());
        }
        
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {
        
        log.error("Erro de integridade de dados: {}", ex.getMessage());
        conflictErrorCounter.increment();
        
        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT.value(),
                "Conflito de dados",
                "Não foi possível processar a solicitação devido a conflitos com os dados existentes",
                request.getRequestURI()
        );
        
        if (ex.getCause() != null) {
            apiError.addDetail(ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getCause().getMessage());
        }
        
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        
        log.error("Erro não tratado: {}", ex.getMessage(), ex);
        serverErrorCounter.increment();
        
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno",
                "Ocorreu um erro inesperado ao processar a solicitação",
                request.getRequestURI()
        );
        
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 