package br.com.fiap.tech.scheduling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NearbyFacilityResponse {
    private FacilityResponse facility;
    private Double distanceInKm;
}
