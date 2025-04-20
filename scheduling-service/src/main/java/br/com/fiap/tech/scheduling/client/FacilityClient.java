package br.com.fiap.tech.scheduling.client;

import br.com.fiap.tech.scheduling.config.FeignConfig;
import br.com.fiap.tech.scheduling.dto.NearbyFacilityResponse;
import br.com.fiap.tech.scheduling.dto.DoctorScheduleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;
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
            
    @GetMapping("/api/v1/facilities/{facilityId}/current-load")
    Integer getCurrentLoad(@PathVariable("facilityId") Long facilityId);

    @GetMapping("/api/v1/facilities/{facilityId}/max-capacity")
    Integer getMaxDailyCapacity(@PathVariable("facilityId") Long facilityId);

    @PutMapping("/api/v1/facilities/{facilityId}/current-load")
    void updateCurrentLoad(
        @PathVariable("facilityId") Long facilityId,
        @RequestParam("increment") Boolean increment
    );

    @GetMapping("/api/v1/facilities/{facilityId}/available")
    Boolean checkAvailabilityForDate(
        @PathVariable("facilityId") Long facilityId,
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    );
    
    @GetMapping("/api/v1/facilities/schedules/doctor/{doctorId}")
    List<DoctorScheduleDTO> getDoctorSchedules(@PathVariable("doctorId") Long doctorId);
}
