package br.com.fiap.tech.scheduling.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class ValidationError extends ApiError {
    private Map<String, String> details;
    
    public ValidationError(int status, String error, String message, LocalDateTime timestamp, Map<String, String> details) {
        super(status, error, message, timestamp);
        this.details = details;
    }
} 