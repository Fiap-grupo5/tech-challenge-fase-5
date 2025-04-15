package br.com.fiap.tech.people.dto;

import lombok.Data;

@Data
public class DoctorRequest {
    private String fullName;
    private String cpf;
    private String crm;
    private String specialty;
    private String email;
    private String phoneNumber;
}
