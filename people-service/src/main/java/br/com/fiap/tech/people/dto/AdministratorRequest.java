package br.com.fiap.tech.people.dto;

import lombok.Data;

@Data
public class AdministratorRequest {
    private String fullName;
    private String cpf;
    private String phoneNumber;
}
