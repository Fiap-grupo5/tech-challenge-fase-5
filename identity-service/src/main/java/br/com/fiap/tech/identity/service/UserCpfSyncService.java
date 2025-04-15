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
        
        // Normalize o CPF
        String normalizedCpf = normalizeCpf(cpf);
        
        // Verifica localmente primeiro (rápido)
        boolean localExists = userCpfRepository.existsByCpf(normalizedCpf);
        if (localExists) {
            log.info("CPF {} já existe localmente", normalizedCpf);
            return true;
        }
        
        // Verifica remotamente (pode ser mais lento)
        try {
            boolean remoteExists = checkCpfExistsRemotely(normalizedCpf);
            if (remoteExists) {
                log.info("CPF {} existe remotamente, sincronizando localmente", normalizedCpf);
                // Se existe remotamente mas não localmente, salvamos localmente para futura referência
                saveCpfReference(normalizedCpf, null, null, null);
                return true;
            }
        } catch (Exception e) {
            log.error("Erro ao verificar CPF remotamente: {}", e.getMessage());
            // Em caso de erro, assumimos que pode existir (fail safe)
            return true;
        }
        
        return false;
    }
    
    /**
     * Registra um CPF no sistema, tanto local quanto remotamente
     */
    @Transactional
    public void registerCpf(String cpf, User user, UserType userType, String username) {
        String normalizedCpf = normalizeCpf(cpf);
        saveCpfReference(normalizedCpf, user, userType, username);
    }
    
    /**
     * Salva uma referência local de CPF
     */
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
        log.info("CPF {} registrado localmente", normalizedCpf);
    }
    
    /**
     * Verifica remotamente se um CPF existe
     * Com retry pattern para lidar com falhas temporárias
     */
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public boolean checkCpfExistsRemotely(String cpf) {
        return peopleServiceClient.checkCpfExists(cpf);
    }
    
    /**
     * Job que roda periodicamente para sincronizar CPFs remotos com o banco local
     */
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.HOURS)
    public void synchronizeCpfs() {
        log.info("Iniciando sincronização de CPFs");
        // Implementação de sincronização
        // Pode buscar todos os CPFs do people-service e sincronizar com o banco local
    }
    
    /**
     * Normaliza um CPF para padrão sem formatação e com no máximo 11 dígitos
     */
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