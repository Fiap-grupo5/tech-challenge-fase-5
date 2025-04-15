package br.com.fiap.tech.people.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequest {
    private String fullName;
    private String cpf;
    private String nationalHealthCard;
    private String email;
    private LocalDate birthDate;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String zipCode;
}
