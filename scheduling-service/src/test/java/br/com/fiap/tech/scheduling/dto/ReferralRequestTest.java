package br.com.fiap.tech.scheduling.dto;

import br.com.fiap.tech.scheduling.domain.PriorityLevel;
import br.com.fiap.tech.scheduling.domain.ReferralType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReferralRequestTest {

    private ReferralRequest createReferralRequest() {
        ReferralRequest referralRequest = new ReferralRequest();
        referralRequest.setReferralReason("Routine check-up");
        referralRequest.setPriorityLevel(PriorityLevel.HIGH);
        referralRequest.setPatientId(1L);
        referralRequest.setRequestedByDoctorId(2L);
        referralRequest.setReferralType(ReferralType.SPECIALIST);
        referralRequest.setPatientAge(30);
        referralRequest.setIsPregnant(false);
        referralRequest.setHasMedicalUrgency(true);
        return referralRequest;
    }

    @Test
    void shouldSetAndGetFields() {
        ReferralRequest referralRequest = createReferralRequest();

        assertThat(referralRequest.getReferralReason()).isEqualTo("Routine check-up");
        assertThat(referralRequest.getPriorityLevel()).isEqualTo(PriorityLevel.HIGH);
        assertThat(referralRequest.getPatientId()).isEqualTo(1L);
        assertThat(referralRequest.getRequestedByDoctorId()).isEqualTo(2L);
        assertThat(referralRequest.getReferralType()).isEqualTo(ReferralType.SPECIALIST);
        assertThat(referralRequest.getPatientAge()).isEqualTo(30);
        assertThat(referralRequest.getIsPregnant()).isFalse();
        assertThat(referralRequest.getHasMedicalUrgency()).isTrue();
    }

    @Test
    void shouldVerifyEqualsAndHashCode() {
        ReferralRequest referralRequest1 = createReferralRequest();
        ReferralRequest referralRequest2 = createReferralRequest();

        assertThat(referralRequest1)
                .isEqualTo(referralRequest2)
                .hasSameHashCodeAs(referralRequest2);
    }

    @Test
    void shouldVerifyToString() {
        ReferralRequest referralRequest = createReferralRequest();

        String toString = referralRequest.toString();
        assertThat(toString).contains("Routine check-up", "HIGH", "1", "2", "SPECIALIST", "30", "false", "true");
    }
}