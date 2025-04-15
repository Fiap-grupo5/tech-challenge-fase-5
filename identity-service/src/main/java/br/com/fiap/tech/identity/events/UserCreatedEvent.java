package br.com.fiap.tech.identity.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent {
    private Long userId;
    private String username;
    private String userType;
    
    // Required fields for all user types
    private String fullName;
    private String cpf;
    private String email;
    
    // Required fields specific for Doctor
    private String crm;
    private String specialty;
    
    // Required fields specific for Patient
    private String nationalHealthCard;
    
    // Optional fields
    private String phoneNumber;
    private LocalDate birthDate;
    private String address;
    private String city;
    private String state;
    private String zipCode;
}
