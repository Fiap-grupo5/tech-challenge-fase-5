package br.com.fiap.tech.scheduling.events;

import br.com.fiap.tech.scheduling.domain.ReferralType;
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
    private ReferralType referralType;
    private String priorityLevel;
}
