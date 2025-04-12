package br.com.fiap.tech.scheduling.domain;

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
@Table(name = "referrals")
public class Referral {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String referralReason;
    
    @Column(nullable = false)
    private String priorityLevel;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReferralStatus status;
    
    @Column(nullable = false)
    private LocalDateTime requestedDate;
    
    @Column(nullable = false)
    private Long patientId;
    
    @Column(nullable = false)
    private Long requestedByDoctorId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReferralType referralType;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        requestedDate = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
