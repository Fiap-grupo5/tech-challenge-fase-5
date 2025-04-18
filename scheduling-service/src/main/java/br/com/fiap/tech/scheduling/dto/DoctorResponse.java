package br.com.fiap.tech.scheduling.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DoctorResponse {
    private Long id;
    private String name;
    private String email;
    private String crm;
    private String specialty;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 