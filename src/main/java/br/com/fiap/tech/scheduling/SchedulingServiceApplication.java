package br.com.fiap.tech.scheduling;

import br.com.fiap.tech.scheduling.config.FeignConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(
    basePackages = "br.com.fiap.tech.scheduling.client",
    defaultConfiguration = FeignConfiguration.class
)
public class SchedulingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulingServiceApplication.class, args);
    }
} 