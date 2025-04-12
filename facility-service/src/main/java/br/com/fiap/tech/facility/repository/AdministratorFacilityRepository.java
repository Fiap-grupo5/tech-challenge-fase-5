package br.com.fiap.tech.facility.repository;

import br.com.fiap.tech.facility.domain.AdministratorFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdministratorFacilityRepository extends JpaRepository<AdministratorFacility, Long> {
    List<AdministratorFacility> findByAdministratorId(Long administratorId);
    
    List<AdministratorFacility> findByHealthcareFacilityId(Long healthcareFacilityId);
}
