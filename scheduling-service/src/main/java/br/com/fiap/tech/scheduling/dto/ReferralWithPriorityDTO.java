package br.com.fiap.tech.scheduling.dto;

import br.com.fiap.tech.scheduling.domain.Referral;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReferralWithPriorityDTO {
    private Referral referral;
    private Integer priorityScore;
    private Integer estimatedWaitingDays;
    
    public ReferralWithPriorityDTO(Referral referral, Integer priorityScore) {
        this.referral = referral;
        this.priorityScore = priorityScore;
        
        // Estimativa simples baseada na pontuação
        if (priorityScore >= 100) {
            this.estimatedWaitingDays = 1; // Muito urgente
        } else if (priorityScore >= 70) {
            this.estimatedWaitingDays = 3; // Alta prioridade
        } else if (priorityScore >= 40) {
            this.estimatedWaitingDays = 7; // Média prioridade
        } else {
            this.estimatedWaitingDays = 14; // Baixa prioridade
        }
    }
} 