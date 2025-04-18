package br.com.fiap.tech.scheduling.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "br.com.fiap.tech.scheduling.client")
public class OpenFeignConfig {
} 