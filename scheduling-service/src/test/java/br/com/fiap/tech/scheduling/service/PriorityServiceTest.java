package br.com.fiap.tech.scheduling.service;

import br.com.fiap.tech.scheduling.domain.PriorityLevel;
import br.com.fiap.tech.scheduling.domain.Referral;
import br.com.fiap.tech.scheduling.domain.ReferralStatus;
import br.com.fiap.tech.scheduling.domain.ReferralType;
import br.com.fiap.tech.scheduling.dto.ReferralWithPriorityDTO;
import br.com.fiap.tech.scheduling.repository.ReferralRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class PriorityServiceTest {

    @Mock
    private ReferralRepository referralRepository;

    @InjectMocks
    private PriorityService priorityService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    private Referral createReferral(Long id, PriorityLevel priorityLevel, ReferralType type, int waitingDays, Integer age, Boolean isPregnant, Boolean hasUrgency) {
        Referral referral = new Referral();
        referral.setId(id);
        referral.setPriorityLevel(priorityLevel);
        referral.setReferralType(type);
        referral.setWaitingTimeInDays(waitingDays);
        referral.setPatientAge(age);
        referral.setIsPregnant(isPregnant);
        referral.setHasMedicalUrgency(hasUrgency);
        referral.setStatus(ReferralStatus.PENDING);
        return referral;
    }

    @Test
    void shouldCalculatePriorityScore() {
        Referral referral = createReferral(1L, PriorityLevel.HIGH, ReferralType.SPECIALIST, 5, 70, false, true);
        int score = priorityService.calculatePriorityScore(referral);

        assertThat(score).isEqualTo(70 + 20 + 10); // HIGH + Urgência médica + Idoso
    }

    @Test
    void shouldGetPrioritizedReferrals() {
        Referral referral1 = createReferral(1L, PriorityLevel.HIGH, ReferralType.SPECIALIST, 5, 70, false, true);
        Referral referral2 = createReferral(2L, PriorityLevel.MEDIUM, ReferralType.LAB, 10, 30, true, false);

        when(referralRepository.findByStatus(ReferralStatus.PENDING))
                .thenReturn(Arrays.asList(referral1, referral2));

        List<ReferralWithPriorityDTO> result = priorityService.getPrioritizedReferrals();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getReferral()).isEqualTo(referral1);
        assertThat(result.get(1).getReferral()).isEqualTo(referral2);
    }

    @Test
    void shouldGetPrioritizedReferralsByType() {
        Referral referral = createReferral(1L, PriorityLevel.LOW, ReferralType.LAB, 7, 25, false, false);

        when(referralRepository.findByReferralTypeAndStatus(ReferralType.LAB, ReferralStatus.PENDING))
                .thenReturn(Collections.singletonList(referral));

        List<ReferralWithPriorityDTO> result = priorityService.getPrioritizedReferralsByType(ReferralType.LAB);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getReferral()).isEqualTo(referral);
    }

    @Test
    void shouldGetNextHighestPriorityReferral() {
        Referral referral1 = createReferral(1L, PriorityLevel.HIGH, ReferralType.SPECIALIST, 5, 70, false, true);
        Referral referral2 = createReferral(2L, PriorityLevel.MEDIUM, ReferralType.LAB, 10, 30, true, false);

        when(referralRepository.findByStatus(ReferralStatus.PENDING))
                .thenReturn(Arrays.asList(referral1, referral2));

        ReferralWithPriorityDTO result = priorityService.getNextHighestPriorityReferral();

        assertThat(result).isNotNull();
        assertThat(result.getReferral()).isEqualTo(referral1);
    }

    @Test
    void shouldReturnNullWhenNoReferralsAvailable() {
        when(referralRepository.findByStatus(ReferralStatus.PENDING))
                .thenReturn(Collections.emptyList());

        ReferralWithPriorityDTO result = priorityService.getNextHighestPriorityReferral();

        assertThat(result).isNull();
    }
}