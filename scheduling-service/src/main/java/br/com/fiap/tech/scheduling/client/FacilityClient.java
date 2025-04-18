package br.com.fiap.tech.scheduling.client;

import br.com.fiap.tech.scheduling.config.FeignConfig;
import br.com.fiap.tech.scheduling.dto.NearbyFacilityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
    name = "scheduling-facility-client", 
    url = "${services.facility.url}",
    configuration = FeignConfig.class
)
public interface FacilityClient {

    @GetMapping("/api/v1/facilities/nearby")
    List<NearbyFacilityResponse> findNearbyFacilities(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(required = false) Double radiusInKm,
            @RequestParam(required = false) Integer limit);
}
