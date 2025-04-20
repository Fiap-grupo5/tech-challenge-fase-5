package br.com.fiap.tech.scheduling.repository;

import br.com.fiap.tech.scheduling.domain.Referral;
import br.com.fiap.tech.scheduling.domain.ReferralStatus;
import br.com.fiap.tech.scheduling.domain.ReferralType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReferralRepository extends JpaRepository<Referral, Long> {
    
    List<Referral> findByPatientId(Long patientId);
    
    List<Referral> findByRequestedByDoctorId(Long doctorId);
    
    List<Referral> findByPatientIdAndStatus(Long patientId, ReferralStatus status);
    
    List<Referral> findByStatus(ReferralStatus status);
    
    List<Referral> findByReferralTypeAndStatus(ReferralType referralType, ReferralStatus status);
    
    @Query("SELECT r FROM Referral r WHERE r.status = :status ORDER BY " +
           "CASE r.priorityLevel " +
           "    WHEN 'URGENT' THEN 0 " +
           "    WHEN 'HIGH' THEN 1 " +
           "    WHEN 'MEDIUM' THEN 2 " +
           "    WHEN 'LOW' THEN 3 " +
           "END, r.requestedDate ASC")
    List<Referral> findByStatusOrderedByPriority(@Param("status") ReferralStatus status);
}
