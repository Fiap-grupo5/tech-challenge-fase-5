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
                log.info("=== EVENT RECEIVED === userCreatedInput: userId={}, userType={}, fullName={}", 
                        event.getUserId(), event.getUserType(), event.getFullName());
                
                if (event.getUserId() == null) {
                    log.error("Event validation failed: userId is null");
                    return;
                }
                if (event.getUserType() == null) {
                    log.error("Event validation failed: userType is null for userId={}", event.getUserId());
                    return;
                }
                
                log.debug("Processing user creation with data: fullName={}, cpf={}, userType={}", 
                        event.getFullName(), event.getCpf(), event.getUserType());
                
                peopleService.handleUserCreated(event);
                log.info("User creation event processed successfully: userId={}, userType={}", 
                        event.getUserId(), event.getUserType());
            } catch (Exception e) {
                log.error("Error while processing user creation event: userId={}, userType={}, error={}", 
                        event.getUserId(), event.getUserType(), e.getMessage(), e);
            }
        };
    }

    @Bean
    public Consumer<UserDeletionEvent> userDeletedInput() {
        return event -> {
            try {
                log.info("=== EVENT RECEIVED === userDeletedInput: userId={}", event.getUserId());
                
                if (event.getUserId() == null) {
                    log.error("Event validation failed: userId is null for deletion");
                    return;
                }
                
                peopleService.handleUserDeleted(event);
                log.info("User deletion event processed successfully.: userId={}", event.getUserId());
            } catch (Exception e) {
                log.error("Error while processing user deletion event: userId={}, error={}", 
                        event.getUserId(), e.getMessage(), e);
            }
        };
    }

}
