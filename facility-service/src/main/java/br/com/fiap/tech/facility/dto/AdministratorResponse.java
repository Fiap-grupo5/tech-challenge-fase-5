package br.com.fiap.tech.facility.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdministratorResponse {
    private Long id;
    private String fullName;
    private String cpf;
    private String phoneNumber;
    private Long userId;
} 