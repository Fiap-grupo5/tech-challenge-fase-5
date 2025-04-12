package br.com.fiap.tech.people.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "doctors")
public class Doctor {
    
    @Id
    private Long id; // Same as user_id from Identity Service
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(nullable = false)
    private String cpf;
    
    @Column(nullable = false)
    private String crm;
    
    @Column(nullable = false)
    private String specialty;
    
    private String phoneNumber;
    
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
