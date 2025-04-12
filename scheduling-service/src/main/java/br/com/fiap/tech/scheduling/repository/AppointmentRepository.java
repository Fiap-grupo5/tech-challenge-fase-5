package br.com.fiap.tech.scheduling.repository;

import br.com.fiap.tech.scheduling.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByPatientId(Long patientId);
    
    List<Appointment> findByDoctorId(Long doctorId);
    
    List<Appointment> findByHealthcareFacilityId(Long healthcareFacilityId);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND a.appointmentDate = :date")
    List<Appointment> findByDoctorIdAndDate(Long doctorId, LocalDate date);
    
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Appointment a " +
           "WHERE a.doctorId = :doctorId AND a.appointmentDate = :date " +
           "AND ((a.startTime <= :endTime AND a.endTime >= :startTime))")
    boolean hasConflictingAppointment(Long doctorId, LocalDate date, LocalTime startTime, LocalTime endTime);
}
