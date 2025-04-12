package br.com.fiap.tech.facility.dto;

import br.com.fiap.tech.facility.domain.FacilityType;
import lombok.Data;

@Data
public class HealthcareFacilityRequest {
    private String name;
    private FacilityType facilityType;
    private String cnpj;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private Double latitude;
    private Double longitude;
    private Integer maxDailyCapacity;
}
