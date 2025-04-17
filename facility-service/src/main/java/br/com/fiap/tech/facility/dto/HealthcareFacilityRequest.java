package br.com.fiap.tech.facility.dto;

import br.com.fiap.tech.facility.domain.FacilityType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HealthcareFacilityRequest {
    @Schema(example = "Hospital São Luiz")
    private String name;
    
    @Schema(example = "HOSPITAL")
    private FacilityType facilityType;
    
    @Schema(example = "12345678901234")
    private String cnpj;
    
    @Schema(example = "(11) 98765-4321")
    private String phoneNumber;
    
    @Schema(example = "Av. Paulista, 1000")
    private String address;
    
    @Schema(example = "São Paulo")
    private String city;
    
    @Schema(example = "SP")
    private String state;
    
    @Schema(example = "01310100")
    private String zipCode;
    
    @Schema(example = "-23.5505")
    private Double latitude;
    
    @Schema(example = "-46.6333")
    private Double longitude;
    
    @Schema(example = "100")
    private Integer maxDailyCapacity;
}
