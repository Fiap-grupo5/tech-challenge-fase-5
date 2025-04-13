package br.com.fiap.tech.facility.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReferralCreatedEvent {
    private Long referralId;
    private Long patientId;
    private Long requestedByDoctorId;
    private String referralType;
    private String priorityLevel;
} 