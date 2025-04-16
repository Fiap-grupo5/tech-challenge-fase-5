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
        
        try {
            log.info("Attempting to check if CPF exists: {}", cpf);
            
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

        try {

            log.info("Attempting to check if CRM exists: {}", crm);
            
            return false;
        } catch (Exception e) {
            log.error("Error checking if CRM exists: {}", e.getMessage());
            return false;
        }
    }
} 