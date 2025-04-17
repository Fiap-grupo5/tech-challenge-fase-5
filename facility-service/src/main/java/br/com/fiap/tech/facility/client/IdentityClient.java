package br.com.fiap.tech.facility.client;

import br.com.fiap.tech.facility.dto.AdministratorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "identity-service", url = "${services.identity.url}")
public interface IdentityClient {
    
    @GetMapping("/api/v1/auth/administrators/{id}")
    AdministratorResponse getAdministrator(@PathVariable("id") Long id);
    
    default boolean administratorExists(Long id) {
        try {
            getAdministrator(id);
            return true;
        } catch (Exception e) {
            throw new EntityNotFoundException("Administrador com ID " + id + " não encontrado");
        }
    }
} 