package br.com.fiap.tech.facility.service;

import br.com.fiap.tech.facility.domain.*;
import br.com.fiap.tech.facility.dto.*;
import br.com.fiap.tech.facility.events.AppointmentCreatedEvent;
import br.com.fiap.tech.facility.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final HealthcareFacilityRepository facilityRepository;
    private final DoctorScheduleRepository scheduleRepository;
    private final AdministratorFacilityRepository adminFacilityRepository;

    @Transactional
    public HealthcareFacility createFacility(HealthcareFacilityRequest request) {
        var facility = HealthcareFacility.builder()
                .name(request.getName())
                .facilityType(request.getFacilityType())
                .cnpj(request.getCnpj())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .maxDailyCapacity(request.getMaxDailyCapacity())
                .currentLoad(0)
                .build();

        return facilityRepository.save(facility);
    }

    @Transactional
    public DoctorSchedule createDoctorSchedule(DoctorScheduleRequest request) {
        var schedule = DoctorSchedule.builder()
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .secondPeriodStart(request.getSecondPeriodStart())
                .secondPeriodEnd(request.getSecondPeriodEnd())
                .doctorId(request.getDoctorId())
                .facilityId(request.getFacilityId())
                .build();

        return scheduleRepository.save(schedule);
    }

    @Transactional
    public AdministratorFacility assignAdministratorToFacility(AdministratorFacilityRequest request) {
        var adminFacility = AdministratorFacility.builder()
                .administratorId(request.getAdministratorId())
                .healthcareFacilityId(request.getHealthcareFacilityId())
                .build();

        return adminFacilityRepository.save(adminFacility);
    }

    @Transactional
    public void handleAppointmentCreated(AppointmentCreatedEvent event) {
        var facility = facilityRepository.findById(event.getHealthcareFacilityId())
                .orElseThrow(() -> new EntityNotFoundException("Facility not found"));

        facility.setCurrentLoad(facility.getCurrentLoad() + 1);
        facilityRepository.save(facility);
    }

    public HealthcareFacility getFacility(Long id) {
        return facilityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Facility not found"));
    }

    public List<HealthcareFacility> getAvailableFacilities() {
        return facilityRepository.findAvailableFacilities();
    }

    public List<DoctorSchedule> getDoctorSchedules(Long doctorId) {
        return scheduleRepository.findByDoctorId(doctorId);
    }

    public List<DoctorSchedule> getFacilitySchedules(Long facilityId) {
        return scheduleRepository.findByFacilityId(facilityId);
    }

    public List<AdministratorFacility> getAdministratorFacilities(Long administratorId) {
        return adminFacilityRepository.findByAdministratorId(administratorId);
    }

    public List<AdministratorFacility> getFacilityAdministrators(Long facilityId) {
        return adminFacilityRepository.findByHealthcareFacilityId(facilityId);
    }

    public List<NearbyFacilitiesResponse> findNearbyFacilities(Double latitude, Double longitude, Double radiusInKm, Integer limit) {
        List<HealthcareFacility> allFacilities = facilityRepository.findAll();
        
        return allFacilities.stream()
                .map(facility -> new NearbyFacilitiesResponse(
                        facility,
                        calculateDistance(latitude, longitude, facility.getLatitude(), facility.getLongitude())))
                .filter(response -> response.getDistanceInKm() <= radiusInKm)
                .sorted(java.util.Comparator.comparing(NearbyFacilitiesResponse::getDistanceInKm))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private Double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int R = 6371; // Earth's radius in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
