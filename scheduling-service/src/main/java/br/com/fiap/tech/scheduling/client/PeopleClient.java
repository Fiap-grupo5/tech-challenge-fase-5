package br.com.fiap.tech.scheduling.client;

import br.com.fiap.tech.scheduling.config.FeignConfig;
import br.com.fiap.tech.scheduling.dto.DoctorResponse;
import br.com.fiap.tech.scheduling.dto.PatientResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "scheduling-people-client", 
    url = "${services.people.url}",
    configuration = FeignConfig.class
)
public interface PeopleClient {
    
    @GetMapping("/api/v1/people/doctors/{id}")
    DoctorResponse getDoctor(@PathVariable("id") Long id);
    
    @GetMapping("/api/v1/people/patients/{id}")
    PatientResponse getPatient(@PathVariable("id") Long id);
    
    default boolean doctorExists(Long id) {
        try {
            getDoctor(id);
            return true;
        } catch (Exception e) {
            throw new EntityNotFoundException("Médico com ID " + id + " não encontrado");
        }
    }
    
    default boolean patientExists(Long id) {
        try {
            getPatient(id);
            return true;
        } catch (Exception e) {
            throw new EntityNotFoundException("Paciente com ID " + id + " não encontrado");
        }
    }
} 