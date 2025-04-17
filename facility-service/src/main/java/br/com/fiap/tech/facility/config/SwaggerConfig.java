package br.com.fiap.tech.facility.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {
    
    @Bean
    public OpenAPI springFacilityOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Facility API")
                        .description("API para gerenciamento de unidades de saúde e agendas médicas")
                        .version("v1.0.0")
                        .license(new License().name("FIAP Healthcare").url("https://fiap.com.br")))
                .externalDocs(new ExternalDocumentation()
                        .description("FIAP Healthcare Documentation")
                        .url("https://fiap.com.br/docs"));
    }
} 