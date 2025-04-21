package br.com.fiap.tech.scheduling.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class FacilityResponseTest {

    private FacilityResponse createFacilityResponse() {
        LocalDateTime createdAt = LocalDateTime.of(2025, 5, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 5, 2, 15, 30);

        FacilityResponse facility = new FacilityResponse();
        facility.setId(1L);
        facility.setName("Hospital Brasília");
        facility.setFacilityType("Hospital");
        facility.setCnpj("12.345.678/0001-90");
        facility.setPhoneNumber("(61) 1234-5678");
        facility.setAddress("Quadra 1, Bloco A");
        facility.setCity("Brasília");
        facility.setState("Distrito Federal");
        facility.setZipCode("70000-000");
        facility.setLatitude(-15.7941);
        facility.setLongitude(-47.8825);
        facility.setMaxDailyCapacity(500);
        facility.setCurrentLoad(300);
        facility.setCreatedAt(createdAt);
        facility.setUpdatedAt(updatedAt);

        return facility;
    }

    @Test
    void shouldSetAndGetFields() {
        FacilityResponse facility = createFacilityResponse();

        assertThat(facility.getId()).isEqualTo(1L);
        assertThat(facility.getName()).isEqualTo("Hospital Brasília");
        assertThat(facility.getFacilityType()).isEqualTo("Hospital");
        assertThat(facility.getCnpj()).isEqualTo("12.345.678/0001-90");
        assertThat(facility.getPhoneNumber()).isEqualTo("(61) 1234-5678");
        assertThat(facility.getAddress()).isEqualTo("Quadra 1, Bloco A");
        assertThat(facility.getCity()).isEqualTo("Brasília");
        assertThat(facility.getState()).isEqualTo("Distrito Federal");
        assertThat(facility.getZipCode()).isEqualTo("70000-000");
        assertThat(facility.getLatitude()).isEqualTo(-15.7941);
        assertThat(facility.getLongitude()).isEqualTo(-47.8825);
        assertThat(facility.getMaxDailyCapacity()).isEqualTo(500);
        assertThat(facility.getCurrentLoad()).isEqualTo(300);
        assertThat(facility.getCreatedAt()).isEqualTo(LocalDateTime.of(2025, 5, 1, 10, 0));
        assertThat(facility.getUpdatedAt()).isEqualTo(LocalDateTime.of(2025, 5, 2, 15, 30));
    }

    @Test
    void shouldVerifyEqualsAndHashCode() {
        FacilityResponse facility1 = createFacilityResponse();
        FacilityResponse facility2 = createFacilityResponse();

        assertThat(facility1)
                .isEqualTo(facility2)
                .hasSameHashCodeAs(facility2);
    }

    @Test
    void shouldVerifyToString() {
        FacilityResponse facility = createFacilityResponse();

        String toString = facility.toString();
        assertThat(toString).contains("1", "Hospital Brasília", "Hospital", "12.345.678/0001-90", "(61) 1234-5678",
                "Quadra 1, Bloco A", "Brasília", "Distrito Federal", "70000-000", "-15.7941", "-47.8825", "500", "300",
                LocalDateTime.of(2025, 5, 1, 10, 0).toString(), LocalDateTime.of(2025, 5, 2, 15, 30).toString());
    }
}