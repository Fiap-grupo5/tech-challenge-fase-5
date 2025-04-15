package br.com.fiap.tech.identity.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "people-service", url = "${people.service.url:http://people-service:8082}")
public interface PeopleServiceClient {
    
    @GetMapping("/api/v1/people/check-cpf")
    boolean checkCpfExists(@RequestParam("cpf") String cpf);
    
    @GetMapping("/api/v1/people/check-crm")
    boolean checkCrmExists(@RequestParam("crm") String crm);
} 