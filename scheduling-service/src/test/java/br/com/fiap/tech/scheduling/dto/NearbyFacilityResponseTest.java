package br.com.fiap.tech.scheduling.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NearbyFacilityResponseTest {

    @Test
    void shouldSetAndGetFields() {
        FacilityResponse facility = new FacilityResponse();
        facility.setId(1L);
        facility.setName("Hospital Brasília");

        NearbyFacilityResponse nearbyFacility = new NearbyFacilityResponse();
        nearbyFacility.setFacility(facility);
        nearbyFacility.setDistanceInKm(5.0);

        assertThat(nearbyFacility.getFacility()).isEqualTo(facility);
        assertThat(nearbyFacility.getDistanceInKm()).isEqualTo(5.0);
    }

    @Test
    void shouldVerifyEqualsAndHashCode() {
        FacilityResponse facility = new FacilityResponse();
        facility.setId(1L);
        facility.setName("Hospital Brasília");

        NearbyFacilityResponse nearbyFacility1 = new NearbyFacilityResponse(facility, 5.0);
        NearbyFacilityResponse nearbyFacility2 = new NearbyFacilityResponse(facility, 5.0);

        assertThat(nearbyFacility1)
                .isEqualTo(nearbyFacility2)
                .hasSameHashCodeAs(nearbyFacility2);
    }

    @Test
    void shouldVerifyToString() {
        FacilityResponse facility = new FacilityResponse();
        facility.setId(1L);
        facility.setName("Hospital Brasília");

        NearbyFacilityResponse nearbyFacility = new NearbyFacilityResponse(facility, 5.0);

        String toString = nearbyFacility.toString();
        assertThat(toString).contains("Hospital Brasília", "5.0");
    }
}