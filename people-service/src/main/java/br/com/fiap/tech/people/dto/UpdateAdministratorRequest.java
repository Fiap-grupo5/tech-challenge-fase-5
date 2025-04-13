package br.com.fiap.tech.people.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdministratorRequest {
    private Long id;
    private String fullName;
    private String cpf;
    private String phoneNumber;
} 