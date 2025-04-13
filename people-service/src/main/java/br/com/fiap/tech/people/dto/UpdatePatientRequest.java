package br.com.fiap.tech.people.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePatientRequest {
    private Long id;
    private String fullName;
    private String cpf;
    private String nationalHealthCard;
    private String phoneNumber;
    private LocalDate birthDate;
    private String address;
    private String city;
    private String state;
    private String zipCode;
} 