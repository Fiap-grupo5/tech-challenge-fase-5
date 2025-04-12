package br.com.fiap.tech.scheduling.repository;

import br.com.fiap.tech.scheduling.domain.Referral;
import br.com.fiap.tech.scheduling.domain.ReferralStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReferralRepository extends JpaRepository<Referral, Long> {
    
    List<Referral> findByPatientId(Long patientId);
    
    List<Referral> findByRequestedByDoctorId(Long doctorId);
    
    List<Referral> findByPatientIdAndStatus(Long patientId, ReferralStatus status);
}
