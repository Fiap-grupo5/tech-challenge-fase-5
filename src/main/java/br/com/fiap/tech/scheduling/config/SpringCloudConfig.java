package br.com.fiap.tech.scheduling.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient(autoRegister = false)
@LoadBalancerClients
@EnableAutoConfiguration
public class SpringCloudConfig {
    // Esta classe ajuda a garantir o carregamento correto de configurações do Spring Cloud
} 