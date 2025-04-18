package br.com.fiap.tech.scheduling.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PatientResponse {
    private Long id;
    private String name;
    private String email;
    private String cpf;
    private LocalDate birthDate;
    private String phoneNumber;
    private String address;
    private String insuranceProvider;
    private String insuranceNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 