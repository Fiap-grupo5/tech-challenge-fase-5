package br.com.fiap.tech.facility.controller;

import br.com.fiap.tech.facility.domain.HealthcareFacility;
import br.com.fiap.tech.facility.dto.FacilityRequest;
import br.com.fiap.tech.facility.dto.NearbyFacilityResponse;
import br.com.fiap.tech.facility.service.FacilityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FacilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacilityService facilityService;

    private FacilityRequest facilityRequest;
    private HealthcareFacility facility;
    private NearbyFacilityResponse nearbyFacility;

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

        nearbyFacility = new NearbyFacilityResponse();
        nearbyFacility.setId(1L);
        nearbyFacility.setName("Hospital São Paulo");
        nearbyFacility.setDistance(0.5);
    }

    @Test
    void createFacility_Success() throws Exception {
        when(facilityService.createFacility(any(FacilityRequest.class)))
                .thenReturn(facility);

        mockMvc.perform(post("/api/v1/facilities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(facilityRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hospital São Paulo"))
                .andExpect(jsonPath("$.type").value("HOSPITAL"));
    }

    @Test
    void getFacility_Success() throws Exception {
        when(facilityService.getFacility(1L)).thenReturn(facility);

        mockMvc.perform(get("/api/v1/facilities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hospital São Paulo"))
                .andExpect(jsonPath("$.type").value("HOSPITAL"));
    }

    @Test
    void getAllFacilities_Success() throws Exception {
        when(facilityService.getAllFacilities()).thenReturn(Arrays.asList(facility));

        mockMvc.perform(get("/api/v1/facilities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Hospital São Paulo"))
                .andExpect(jsonPath("$[0].type").value("HOSPITAL"));
    }

    @Test
    void findNearbyFacilities_Success() throws Exception {
        when(facilityService.findNearbyFacilities(-23.5968, -46.6425, 5.0))
                .thenReturn(Arrays.asList(nearbyFacility));

        mockMvc.perform(get("/api/v1/facilities/nearby")
                        .param("latitude", "-23.5968")
                        .param("longitude", "-46.6425")
                        .param("radius", "5.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Hospital São Paulo"))
                .andExpect(jsonPath("$[0].distance").value(0.5));
    }

    @Test
    void getFacilitiesByType_Success() throws Exception {
        when(facilityService.getFacilitiesByType("HOSPITAL"))
                .thenReturn(Arrays.asList(facility));

        mockMvc.perform(get("/api/v1/facilities/type/HOSPITAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Hospital São Paulo"))
                .andExpect(jsonPath("$[0].type").value("HOSPITAL"));
    }
}
