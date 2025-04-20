package br.com.fiap.tech.scheduling.dto;

import br.com.fiap.tech.scheduling.domain.PriorityLevel;
import br.com.fiap.tech.scheduling.domain.ReferralType;
import lombok.Data;

@Data
public class ReferralRequest {
    private String referralReason;
    private PriorityLevel priorityLevel;
    private Long patientId;
    private Long requestedByDoctorId;
    private ReferralType referralType;
    private Integer patientAge;
    private Boolean isPregnant;
    private Boolean hasMedicalUrgency;
}
