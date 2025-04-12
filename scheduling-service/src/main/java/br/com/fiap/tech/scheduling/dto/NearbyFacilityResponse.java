package br.com.fiap.tech.scheduling.dto;

import lombok.Data;

@Data
public class NearbyFacilityResponse {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String phoneNumber;
    private Double latitude;
    private Double longitude;
    private Integer maxDailyCapacity;
    private Integer currentLoad;
    private Double distanceInKm;
}
