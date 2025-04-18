package br.com.fiap.tech.scheduling.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FacilityResponse {
    private Long id;
    private String name;
    private String facilityType;
    private String cnpj;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private Double latitude;
    private Double longitude;
    private Integer maxDailyCapacity;
    private Integer currentLoad;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 