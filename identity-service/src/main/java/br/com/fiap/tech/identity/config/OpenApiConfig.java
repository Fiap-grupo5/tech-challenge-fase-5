package br.com.fiap.tech.identity.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Identity Service API")
                        .description("API for managing user authentication and authorization")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("FIAP Healthcare")
                                .email("healthcare@fiap.com.br")));
    }
}
