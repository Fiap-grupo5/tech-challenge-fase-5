package br.com.fiap.tech.facility.events;

import br.com.fiap.tech.facility.service.FacilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppointmentEventConsumer {

    private final FacilityService facilityService;

    @Bean
    public Consumer<AppointmentCreatedEvent> appointmentCreatedInput() {
        return event -> {
            log.info("Received AppointmentCreatedEvent for facility: {}", event.getHealthcareFacilityId());
            facilityService.handleAppointmentCreated(event);
        };
    }
}
