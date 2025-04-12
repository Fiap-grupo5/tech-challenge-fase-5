package br.com.fiap.tech.facility.dto;

import br.com.fiap.tech.facility.domain.HealthcareFacility;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NearbyFacilitiesResponse {
    private HealthcareFacility facility;
    private Double distanceInKm;
}
