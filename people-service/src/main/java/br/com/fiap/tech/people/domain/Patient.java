package br.com.fiap.tech.people.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patients")
public class Patient {
    
    @Id
    private Long id; // Same as user_id from Identity Service
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(nullable = false)
    private String cpf;
    
    @Column(name = "national_health_card")
    private String nationalHealthCard; // CNS - Cartão SUS
    
    private LocalDate birthDate;
    
    private String phoneNumber;
    
    private String address;
    
    private String city;
    
    private String state;
    
    private String zipCode;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
