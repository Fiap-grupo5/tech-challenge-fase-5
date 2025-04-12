package br.com.fiap.tech.facility.repository;

import br.com.fiap.tech.facility.domain.HealthcareFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HealthcareFacilityRepository extends JpaRepository<HealthcareFacility, Long> {
    Optional<HealthcareFacility> findByCnpj(String cnpj);
    
    List<HealthcareFacility> findByCity(String city);
    
    @Query("SELECT h FROM HealthcareFacility h WHERE h.currentLoad < h.maxDailyCapacity")
    List<HealthcareFacility> findAvailableFacilities();
}
