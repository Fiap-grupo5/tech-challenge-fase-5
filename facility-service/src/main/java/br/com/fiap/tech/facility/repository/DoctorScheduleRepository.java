package br.com.fiap.tech.facility.repository;

import br.com.fiap.tech.facility.domain.DayOfWeek;
import br.com.fiap.tech.facility.domain.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    List<DoctorSchedule> findByDoctorId(Long doctorId);
    
    List<DoctorSchedule> findByFacilityId(Long facilityId);
    
    List<DoctorSchedule> findByDoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);
    
    List<DoctorSchedule> findByFacilityIdAndDayOfWeek(Long facilityId, DayOfWeek dayOfWeek);
}
