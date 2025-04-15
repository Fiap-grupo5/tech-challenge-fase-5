package br.com.fiap.tech.people.events;

import br.com.fiap.tech.people.service.PeopleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserEventConsumer {

    private final PeopleService peopleService;

    @Bean
    public Consumer<UserCreatedEvent> userCreatedInput() {
        return event -> {
            try {
                log.info("=== EVENTO RECEBIDO === userCreatedInput: userId={}, userType={}, fullName={}", 
                        event.getUserId(), event.getUserType(), event.getFullName());
                
                // Validar campos obrigatórios antes de processar
                if (event.getUserId() == null) {
                    log.error("Event validation failed: userId is null");
                    return;
                }
                if (event.getUserType() == null) {
                    log.error("Event validation failed: userType is null for userId={}", event.getUserId());
                    return;
                }
                
                log.debug("Processando criação de usuário com dados: fullName={}, cpf={}, userType={}", 
                        event.getFullName(), event.getCpf(), event.getUserType());
                
                // Chamar o serviço para processar o evento
                peopleService.handleUserCreated(event);
                log.info("Evento de criação de usuário processado com sucesso: userId={}, userType={}", 
                        event.getUserId(), event.getUserType());
            } catch (Exception e) {
                log.error("Erro ao processar evento de criação de usuário: userId={}, userType={}, error={}", 
                        event.getUserId(), event.getUserType(), e.getMessage(), e);
                // Não propagamos a exceção para evitar que o evento vá para a DLQ
                // Mas logamos para diagnóstico
            }
        };
    }

    @Bean
    public Consumer<UserDeletionEvent> userDeletedInput() {
        return event -> {
            try {
                log.info("=== EVENTO RECEBIDO === userDeletedInput: userId={}", event.getUserId());
                
                if (event.getUserId() == null) {
                    log.error("Event validation failed: userId is null for deletion");
                    return;
                }
                
                peopleService.handleUserDeleted(event);
                log.info("Evento de exclusão de usuário processado com sucesso: userId={}", event.getUserId());
            } catch (Exception e) {
                log.error("Erro ao processar evento de exclusão de usuário: userId={}, error={}", 
                        event.getUserId(), e.getMessage(), e);
                // Não propagamos a exceção para evitar que o evento vá para a DLQ
            }
        };
    }

}
