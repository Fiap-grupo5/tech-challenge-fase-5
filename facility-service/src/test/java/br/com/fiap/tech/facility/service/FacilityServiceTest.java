package br.com.fiap.tech.facility.service;

import br.com.fiap.tech.facility.domain.HealthcareFacility;
import br.com.fiap.tech.facility.dto.FacilityRequest;
import br.com.fiap.tech.facility.dto.NearbyFacilityResponse;
import br.com.fiap.tech.facility.repository.FacilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacilityServiceTest {

    @Mock
    private FacilityRepository facilityRepository;

    @InjectMocks
    private FacilityService facilityService;

    private FacilityRequest facilityRequest;
    private HealthcareFacility facility;

    @BeforeEach
    void setUp() {
        facilityRequest = new FacilityRequest();
        facilityRequest.setName("Hospital São Paulo");
        facilityRequest.setAddress("Rua Napoleão de Barros, 715");
        facilityRequest.setLatitude(-23.5968);
        facilityRequest.setLongitude(-46.6425);
        facilityRequest.setPhoneNumber("11999990001");
        facilityRequest.setType("HOSPITAL");

        facility = new HealthcareFacility();
        facility.setId(1L);
        facility.setName("Hospital São Paulo");
        facility.setAddress("Rua Napoleão de Barros, 715");
        facility.setLatitude(-23.5968);
        facility.setLongitude(-46.6425);
        facility.setPhoneNumber("11999990001");
        facility.setType("HOSPITAL");
    }

    @Test
    void createFacility_Success() {
        when(facilityRepository.save(any(HealthcareFacility.class))).thenReturn(facility);

        HealthcareFacility result = facilityService.createFacility(facilityRequest);

        assertNotNull(result);
        assertEquals(facility.getName(), result.getName());
        assertEquals(facility.getAddress(), result.getAddress());
        verify(facilityRepository).save(any(HealthcareFacility.class));
    }

    @Test
    void getFacility_Success() {
        when(facilityRepository.findById(anyLong())).thenReturn(Optional.of(facility));

        HealthcareFacility result = facilityService.getFacility(1L);

        assertNotNull(result);
        assertEquals(facility.getId(), result.getId());
        assertEquals(facility.getName(), result.getName());
    }

    @Test
    void getAllFacilities_Success() {
        when(facilityRepository.findAll()).thenReturn(Arrays.asList(facility));

        List<HealthcareFacility> results = facilityService.getAllFacilities();

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(facility.getName(), results.get(0).getName());
    }

    @Test
    void findNearbyFacilities_Success() {
        double currentLat = -23.5968;
        double currentLon = -46.6425;
        double radius = 5.0; // 5km radius

        HealthcareFacility nearbyFacility = new HealthcareFacility();
        nearbyFacility.setId(2L);
        nearbyFacility.setName("Nearby Hospital");
        nearbyFacility.setLatitude(-23.5970);
        nearbyFacility.setLongitude(-46.6430);

        when(facilityRepository.findAll()).thenReturn(Arrays.asList(facility, nearbyFacility));

        List<NearbyFacilityResponse> results = facilityService.findNearbyFacilities(currentLat, currentLon, radius);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertTrue(results.size() <= 2);
        assertTrue(results.stream().anyMatch(f -> f.getDistance() <= radius));
    }

    @Test
    void getFacilitiesByType_Success() {
        when(facilityRepository.findByType(anyString())).thenReturn(Arrays.asList(facility));

        List<HealthcareFacility> results = facilityService.getFacilitiesByType("HOSPITAL");

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("HOSPITAL", results.get(0).getType());
    }
}
