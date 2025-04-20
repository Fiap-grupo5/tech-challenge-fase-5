package br.com.fiap.tech.scheduling.service;

import br.com.fiap.tech.scheduling.domain.PriorityLevel;
import br.com.fiap.tech.scheduling.domain.Referral;
import br.com.fiap.tech.scheduling.domain.ReferralStatus;
import br.com.fiap.tech.scheduling.domain.ReferralType;
import br.com.fiap.tech.scheduling.dto.ReferralWithPriorityDTO;
import br.com.fiap.tech.scheduling.repository.ReferralRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriorityService {
    private final ReferralRepository referralRepository;
    
    /**
     * Calcula a pontuação de prioridade para um encaminhamento
     */
    public int calculatePriorityScore(Referral referral) {
        int score = 0;
        
        // Base score por nível de prioridade
        switch (referral.getPriorityLevel()) {
            case URGENT:
                score += 100;
                break;
            case HIGH:
                score += 70;
                break;
            case MEDIUM:
                score += 40;
                break;
            case LOW:
                score += 10;
                break;
        }
        
        // Adicionar pontos para fatores adicionais
        if (referral.getPatientAge() != null) {
            if (referral.getPatientAge() < 12 || referral.getPatientAge() > 65) {
                score += 10; // Prioridade para crianças e idosos
            }
        }
        
        if (Boolean.TRUE.equals(referral.getIsPregnant())) {
            score += 15; // Prioridade para gestantes
        }
        
        if (Boolean.TRUE.equals(referral.getHasMedicalUrgency())) {
            score += 20; // Prioridade para condições médicas urgentes
        }
        
        // Considerar tempo de espera (máximo 30 pontos)
        int waitingDays = referral.getWaitingTimeInDays();
        score += Math.min(waitingDays * 2, 30);
        
        return score;
    }
    
    /**
     * Obtém todos os encaminhamentos pendentes ordenados por prioridade
     */
    public List<ReferralWithPriorityDTO> getPrioritizedReferrals() {
        // Buscar todos os encaminhamentos pendentes
        List<Referral> pendingReferrals = referralRepository.findByStatus(ReferralStatus.PENDING);
        
        // Calcular pontuação de prioridade e ordenar
        return pendingReferrals.stream()
            .map(ref -> new ReferralWithPriorityDTO(ref, calculatePriorityScore(ref)))
            .sorted(Comparator.comparing(ReferralWithPriorityDTO::getPriorityScore).reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * Obtém encaminhamentos pendentes de um tipo específico, ordenados por prioridade
     */
    public List<ReferralWithPriorityDTO> getPrioritizedReferralsByType(ReferralType type) {
        // Buscar encaminhamentos pendentes do tipo especificado
        List<Referral> pendingReferrals = referralRepository.findByReferralTypeAndStatus(type, ReferralStatus.PENDING);
        
        // Calcular pontuação de prioridade e ordenar
        return pendingReferrals.stream()
            .map(ref -> new ReferralWithPriorityDTO(ref, calculatePriorityScore(ref)))
            .sorted(Comparator.comparing(ReferralWithPriorityDTO::getPriorityScore).reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * Obtém o próximo encaminhamento com maior prioridade
     */
    public ReferralWithPriorityDTO getNextHighestPriorityReferral() {
        List<ReferralWithPriorityDTO> prioritizedList = getPrioritizedReferrals();
        
        if (prioritizedList.isEmpty()) {
            return null;
        }
        
        return prioritizedList.get(0);
    }

    /**
     * Processa a fila de encaminhamentos de alta prioridade
     * Este método poderia ser chamado periodicamente por uma tarefa agendada
     */
    //@Scheduled(fixedRate = 3600000) // Executar a cada hora
    public void processHighPriorityReferrals() {
        log.info("Iniciando processamento automático de encaminhamentos prioritários");
        
        // Buscar todos os encaminhamentos com alta prioridade (score >= 80)
        List<ReferralWithPriorityDTO> highPriorityReferrals = getPrioritizedReferrals().stream()
            .filter(ref -> ref.getPriorityScore() >= 80)
            .collect(Collectors.toList());
        
        log.info("Encontrados {} encaminhamentos de alta prioridade para processamento", 
                 highPriorityReferrals.size());
        
        // Para cada encaminhamento de alta prioridade
        for (ReferralWithPriorityDTO referralDTO : highPriorityReferrals) {
            Referral referral = referralDTO.getReferral();
            try {
                // Em um sistema real, aqui buscaríamos vagas disponíveis e faríamos o agendamento
                // Por enquanto, apenas logamos a informação
                log.info("Processando encaminhamento de alta prioridade: ID={}, PriorityScore={}, " +
                         "Tipo={}, PatientId={}", 
                         referral.getId(), 
                         referralDTO.getPriorityScore(),
                         referral.getReferralType(),
                         referral.getPatientId());
                         
                // Exemplo: para casos URGENT, poderíamos enviar notificações especiais
                if (referral.getPriorityLevel() == PriorityLevel.URGENT) {
                    log.info("Encaminhamento URGENTE detectado! ID={}, PatientId={}", 
                             referral.getId(), referral.getPatientId());
                    
                    // Aqui seria uma boa oportunidade para enviar notificações
                }
                
            } catch (Exception e) {
                log.error("Erro ao processar encaminhamento ID {}: {}", referral.getId(), e.getMessage());
            }
        }
        
        log.info("Processamento de encaminhamentos prioritários concluído");
    }
} 