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
            peopleService.handleUserCreated(event);
        };
    }
}
