package br.com.fiap.tech.facility.client;

import br.com.fiap.tech.facility.dto.AdministratorResponse;
import br.com.fiap.tech.facility.dto.DoctorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "people-service", url = "${services.people.url}")
public interface PeopleClient {
    
    @GetMapping("/api/v1/people/doctors/{id}")
    DoctorResponse getDoctor(@PathVariable("id") Long id);
    
    @GetMapping("/api/v1/people/administrators/{id}")
    AdministratorResponse getAdministrator(@PathVariable("id") Long id);
    
    default boolean doctorExists(Long id) {
        try {
            getDoctor(id);
            return true;
        } catch (Exception e) {
            throw new EntityNotFoundException("Médico com ID " + id + " não encontrado");
        }
    }
    
    default boolean administratorExists(Long id) {
        try {
            getAdministrator(id);
            return true;
        } catch (Exception e) {
            throw new EntityNotFoundException("Administrador com ID " + id + " não encontrado");
        }
    }
} 