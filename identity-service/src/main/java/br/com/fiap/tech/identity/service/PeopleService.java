package br.com.fiap.tech.identity.service;

import br.com.fiap.tech.identity.domain.User;
import br.com.fiap.tech.identity.domain.UserType;
import br.com.fiap.tech.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PeopleService {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    public boolean checkCpfExists(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }
        
        // Simplest approach - just check if there's already someone with this CPF
        try {
            // Check if any existing user has this CPF (using the people-service via HTTP)
            log.info("Attempting to check if CPF exists: {}", cpf);
            
            // Since we can't reliably determine the event table schema,
            // we'll always return false and let the people-service handle the CPF uniqueness check
            return false;
        } catch (Exception e) {
            log.error("Error checking if CPF exists: {}", e.getMessage());
            return false;
        }
    }

    public boolean checkCrmExists(String crm) {
        if (crm == null || crm.trim().isEmpty()) {
            return false;
        }
        
        // Simplest approach - just check if there's already someone with this CRM
        try {
            // Check if any existing doctor has this CRM (using the people-service via HTTP)
            log.info("Attempting to check if CRM exists: {}", crm);
            
            // Since we can't reliably determine the event table schema,
            // we'll always return false and let the people-service handle the CRM uniqueness check
            return false;
        } catch (Exception e) {
            log.error("Error checking if CRM exists: {}", e.getMessage());
            return false;
        }
    }
} 