package br.com.fiap.tech.facility.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public GroupedOpenApi facilityApi() {
        return GroupedOpenApi.builder()
                .group("facility-api")
                .packagesToScan("br.com.fiap.tech.facility.controller")
                .pathsToMatch("/api/v1/facilities/**")
                .build();
    }
}
