package br.com.fiap.tech.facility.service;

import br.com.fiap.tech.facility.domain.HealthcareFacility;
import br.com.fiap.tech.facility.dto.HealthcareFacilityRequest;
import br.com.fiap.tech.facility.repository.HealthcareFacilityRepository;
import br.com.fiap.tech.facility.util.StringSanitizer;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FacilityServiceTest {

    @InjectMocks
    private FacilityService facilityService;

    @Mock
    private HealthcareFacilityRepository facilityRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateFacilitySuccessfully() {
        HealthcareFacilityRequest request = new HealthcareFacilityRequest();
        request.setName("Hospital São Luiz");
        request.setCnpj("12345678901234");
        request.setPhoneNumber("(11) 98765-4321");
        request.setAddress("Av. Paulista, 1000");
        request.setCity("São Paulo");
        request.setState("SP");
        request.setZipCode("01310100");
        request.setLatitude(-23.5505);
        request.setLongitude(-46.6333);
        request.setMaxDailyCapacity(100);

        when(facilityRepository.findByCnpj(request.getCnpj())).thenReturn(Optional.empty());
        when(facilityRepository.save(any(HealthcareFacility.class))).thenAnswer(invocation -> invocation.getArgument(0));

        HealthcareFacility createdFacility = facilityService.createFacility(request);

        assertNotNull(createdFacility);
        assertEquals("Hospital S\u00e3o Luiz", createdFacility.getName());
        verify(facilityRepository, times(1)).save(any(HealthcareFacility.class));
    }

    @Test
    void shouldThrowExceptionWhenFacilityWithCnpjAlreadyExists() {
        HealthcareFacilityRequest request = new HealthcareFacilityRequest();
        request.setCnpj("12345678901234");

        when(facilityRepository.findByCnpj(request.getCnpj())).thenReturn(Optional.of(new HealthcareFacility()));

        assertThrows(DataIntegrityViolationException.class, () -> facilityService.createFacility(request));
        verify(facilityRepository, never()).save(any(HealthcareFacility.class));
    }

    @Test
    void shouldGetFacilityByIdSuccessfully() {
        Long facilityId = 1L;
        HealthcareFacility facility = new HealthcareFacility();
        facility.setId(facilityId);

        when(facilityRepository.findById(facilityId)).thenReturn(Optional.of(facility));

        HealthcareFacility foundFacility = facilityService.getFacility(facilityId);

        assertNotNull(foundFacility);
        assertEquals(facilityId, foundFacility.getId());
        verify(facilityRepository, times(1)).findById(facilityId);
    }

    @Test
    void shouldThrowExceptionWhenFacilityNotFoundById() {
        Long facilityId = 1L;

        when(facilityRepository.findById(facilityId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> facilityService.getFacility(facilityId));
        verify(facilityRepository, times(1)).findById(facilityId);
    }
}