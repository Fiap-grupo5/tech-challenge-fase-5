package br.com.fiap.tech.scheduling.dto;

import br.com.fiap.tech.scheduling.domain.Referral;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReferralWithPriorityDTOTest {

    private Referral createReferral() {
        Referral referral = new Referral();
        // Configure os campos necessários do objeto Referral aqui
        referral.setId(1L);
        referral.setReferralReason("Consulta de rotina");
        return referral;
    }

    @Test
    void shouldSetAndGetFields() {
        Referral referral = createReferral();
        ReferralWithPriorityDTO dto = new ReferralWithPriorityDTO(referral, 80, 3);

        assertThat(dto.getReferral()).isEqualTo(referral);
        assertThat(dto.getPriorityScore()).isEqualTo(80);
        assertThat(dto.getEstimatedWaitingDays()).isEqualTo(3);
    }

    @Test
    void shouldCalculateEstimatedWaitingDaysBasedOnPriorityScore() {
        Referral referral = createReferral();

        ReferralWithPriorityDTO highPriority = new ReferralWithPriorityDTO(referral, 100);
        assertThat(highPriority.getEstimatedWaitingDays()).isEqualTo(1);

        ReferralWithPriorityDTO mediumHighPriority = new ReferralWithPriorityDTO(referral, 70);
        assertThat(mediumHighPriority.getEstimatedWaitingDays()).isEqualTo(3);

        ReferralWithPriorityDTO mediumPriority = new ReferralWithPriorityDTO(referral, 40);
        assertThat(mediumPriority.getEstimatedWaitingDays()).isEqualTo(7);

        ReferralWithPriorityDTO lowPriority = new ReferralWithPriorityDTO(referral, 20);
        assertThat(lowPriority.getEstimatedWaitingDays()).isEqualTo(14);
    }

    @Test
    void shouldVerifyEqualsAndHashCode() {
        Referral referral = createReferral();
        ReferralWithPriorityDTO dto1 = new ReferralWithPriorityDTO(referral, 80, 3);
        ReferralWithPriorityDTO dto2 = new ReferralWithPriorityDTO(referral, 80, 3);

        assertThat(dto1).isEqualTo(dto2).hasSameHashCodeAs(dto2);
    }

    @Test
    void shouldVerifyToString() {
        Referral referral = createReferral();
        ReferralWithPriorityDTO dto = new ReferralWithPriorityDTO(referral, 80, 3);

        String toString = dto.toString();
        assertThat(toString).contains("Referral", "80", "3");
    }
}