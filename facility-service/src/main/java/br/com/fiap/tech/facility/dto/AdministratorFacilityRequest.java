package br.com.fiap.tech.facility.dto;

import lombok.Data;

@Data
public class AdministratorFacilityRequest {
    private Long administratorId;
    private Long healthcareFacilityId;
}
