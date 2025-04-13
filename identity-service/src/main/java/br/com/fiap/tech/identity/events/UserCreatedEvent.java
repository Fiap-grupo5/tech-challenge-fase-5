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
    
    // Campos obrigatórios para todos os tipos
    private String fullName;
    private String cpf;
    
    // Campos obrigatórios específicos para Doctor
    private String crm;
    private String specialty;
    
    // Campos obrigatórios específicos para Patient
    private String nationalHealthCard;
    
    // Campos opcionais
    private String phoneNumber;
    private LocalDate birthDate;
    private String address;
    private String city;
    private String state;
    private String zipCode;
}
