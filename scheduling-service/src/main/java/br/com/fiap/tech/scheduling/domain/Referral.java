package br.com.fiap.tech.scheduling.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PriorityLevel priorityLevel;
    
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
    
    @Column(name = "patient_age")
    private Integer patientAge;
    
    @Column(name = "is_pregnant")
    private Boolean isPregnant;
    
    @Column(name = "has_medical_urgency")
    private Boolean hasMedicalUrgency;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @Transient
    private Integer waitingTimeInDays;
    
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
    
    /**
     * Calcula quantos dias o encaminhamento está aguardando desde a solicitação
     */
    public Integer getWaitingTimeInDays() {
        if (this.requestedDate == null) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(this.requestedDate, LocalDateTime.now());
    }
}
