package br.com.fiap.tech.identity.service;

import br.com.fiap.tech.identity.domain.User;
import br.com.fiap.tech.identity.domain.UserCpf;
import br.com.fiap.tech.identity.domain.UserType;
import br.com.fiap.tech.identity.repository.UserCpfRepository;
import br.com.fiap.tech.identity.service.client.PeopleServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCpfSyncService {

    private final UserCpfRepository userCpfRepository;
    private final PeopleServiceClient peopleServiceClient;
    
    /**
     * Verifica se um CPF já existe, tanto localmente quanto remotamente
     * 
     * @param cpf CPF a ser verificado
     * @return true se o CPF já existe, false caso contrário
     */
    public boolean checkCpfExists(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }
        
        String normalizedCpf = normalizeCpf(cpf);
        
        boolean localExists = userCpfRepository.existsByCpf(normalizedCpf);
        if (localExists) {
            log.info("CPF {} already exists locally.", normalizedCpf);
            return true;
        }
        
        try {
            boolean remoteExists = checkCpfExistsRemotely(normalizedCpf);
            if (remoteExists) {
                log.info("CPF {} exists remotely, synchronizing locally.", normalizedCpf);
                saveCpfReference(normalizedCpf, null, null, null);
                return true;
            }
        } catch (Exception e) {
            log.error("Error while verifying CPF remotely: {}", e.getMessage());
            return true;
        }
        
        return false;
    }
    

    @Transactional
    public void registerCpf(String cpf, User user, UserType userType, String username) {
        String normalizedCpf = normalizeCpf(cpf);
        saveCpfReference(normalizedCpf, user, userType, username);
    }
    

    @Transactional
    public void saveCpfReference(String cpf, User user, UserType userType, String username) {
        String normalizedCpf = normalizeCpf(cpf);
        
        // Se já existe, não faz nada
        if (userCpfRepository.existsByCpf(normalizedCpf)) {
            return;
        }
        
        UserCpf userCpf = UserCpf.builder()
                .cpf(normalizedCpf)
                .user(user)
                .userType(userType != null ? userType : UserType.PATIENT) // Default
                .username(username != null ? username : "unknown") // Default
                .build();
        
        userCpfRepository.save(userCpf);
        log.info("CPF {} registered locally.", normalizedCpf);
    }
    
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public boolean checkCpfExistsRemotely(String cpf) {
        return peopleServiceClient.checkCpfExists(cpf);
    }
    
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.HOURS)
    public void synchronizeCpfs() {
        log.info("Starting CPF synchronization.");

    }

    private String normalizeCpf(String cpf) {
        if (cpf == null) {
            return "";
        }
        String normalized = cpf.replaceAll("[^0-9]", "");
        if (normalized.length() > 11) {
            normalized = normalized.substring(0, 11);
        }
        return normalized;
    }
} 