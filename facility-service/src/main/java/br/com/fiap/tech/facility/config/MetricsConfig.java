package br.com.fiap.tech.facility.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {
    
    @Bean
    public Counter validationErrorCounter(MeterRegistry registry) {
        return Counter.builder("api.errors.validation")
                .description("Número de erros de validação")
                .register(registry);
    }
    
    @Bean
    public Counter notFoundErrorCounter(MeterRegistry registry) {
        return Counter.builder("api.errors.not_found")
                .description("Número de erros de recurso não encontrado")
                .register(registry);
    }
    
    @Bean
    public Counter conflictErrorCounter(MeterRegistry registry) {
        return Counter.builder("api.errors.conflict")
                .description("Número de erros de conflito de dados")
                .register(registry);
    }
    
    @Bean
    public Counter serverErrorCounter(MeterRegistry registry) {
        return Counter.builder("api.errors.server")
                .description("Número de erros internos do servidor")
                .register(registry);
    }
} 