package br.com.fiap.tech.people.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDoctorRequest {
    private Long id;
    private String fullName;
    private String cpf;
    private String crm;
    private String specialty;
    private String email;
    private String phoneNumber;
} 