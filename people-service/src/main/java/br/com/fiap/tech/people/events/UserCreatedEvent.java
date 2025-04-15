package br.com.fiap.tech.people.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent {
    private Long userId;
    private String username;
    private String userType;
    private String fullName;
    private String cpf;
    private String email;
    private String crm;
    private String specialty;
    private String nationalHealthCard;
    private String phoneNumber;
    private LocalDate birthDate;
    private String address;
    private String city;
    private String state;
    private String zipCode;
}
