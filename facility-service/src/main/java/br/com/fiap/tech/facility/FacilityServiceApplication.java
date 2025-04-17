package br.com.fiap.tech.facility;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "br.com.fiap.tech.facility.client")
public class FacilityServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FacilityServiceApplication.class, args);
    }
}
