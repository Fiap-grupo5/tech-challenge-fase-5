package br.com.fiap.tech.scheduling.config;

import br.com.fiap.tech.scheduling.dto.DoctorResponse;
import br.com.fiap.tech.scheduling.dto.NearbyFacilityResponse;
import br.com.fiap.tech.scheduling.dto.PatientResponse;
import feign.Contract;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.util.List;

@Configuration
public class FeignConfiguration {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public Decoder feignDecoder() {
        return new org.springframework.cloud.openfeign.support.SpringDecoder(() -> new org.springframework.boot.autoconfigure.http.HttpMessageConverters(
                new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter()
        ));
    }

    @Bean
    @Primary
    @Scope("prototype")
    public Encoder feignEncoder() {
        return new org.springframework.cloud.openfeign.support.SpringEncoder(() -> new org.springframework.boot.autoconfigure.http.HttpMessageConverters(
                new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter()
        ));
    }

    @Bean
    @Primary
    @Scope("prototype")
    public Contract feignContract() {
        return new org.springframework.cloud.openfeign.support.SpringMvcContract();
    }
} 