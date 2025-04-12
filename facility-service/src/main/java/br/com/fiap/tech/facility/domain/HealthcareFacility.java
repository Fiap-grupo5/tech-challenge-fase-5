package br.com.fiap.tech.facility.domain;

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
@Table(name = "healthcare_facilities")
public class HealthcareFacility {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FacilityType facilityType;
    
    @Column(nullable = false)
    private String cnpj;
    
    private String phoneNumber;
    
    private String address;
    
    private String city;
    
    private String state;
    
    private String zipCode;
    
    private Double latitude;
    
    private Double longitude;
    
    @Column(nullable = false)
    private Integer maxDailyCapacity;
    
    @Column(nullable = false)
    private Integer currentLoad;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (currentLoad == null) {
            currentLoad = 0;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
