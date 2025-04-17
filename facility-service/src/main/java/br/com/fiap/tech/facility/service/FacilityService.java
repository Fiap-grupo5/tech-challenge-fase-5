package br.com.fiap.tech.facility.service;

import br.com.fiap.tech.facility.domain.*;
import br.com.fiap.tech.facility.dto.*;
import br.com.fiap.tech.facility.events.AppointmentCreatedEvent;
import br.com.fiap.tech.facility.repository.*;
import br.com.fiap.tech.facility.client.PeopleClient;
import br.com.fiap.tech.facility.client.IdentityClient;
import br.com.fiap.tech.facility.util.ScheduleTimeUtil;
import br.com.fiap.tech.facility.util.StringSanitizer;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacilityService {

    private final HealthcareFacilityRepository facilityRepository;
    private final DoctorScheduleRepository scheduleRepository;
    private final AdministratorFacilityRepository adminFacilityRepository;
    private final PeopleClient peopleClient;
    private final IdentityClient identityClient;

    @Transactional
    public HealthcareFacility createFacility(HealthcareFacilityRequest request) {
        log.info("Criando unidade de saúde com nome {} e CNPJ {}", request.getName(), request.getCnpj());
        
        // Verificar se já existe uma unidade com o mesmo CNPJ
        facilityRepository.findByCnpj(request.getCnpj()).ifPresent(facility -> {
            throw new DataIntegrityViolationException("Já existe uma unidade de saúde com o CNPJ " + request.getCnpj());
        });
        
        // Sanitizar campos de texto
        String name = StringSanitizer.sanitize(request.getName());
        String cnpj = StringSanitizer.sanitize(request.getCnpj());
        String phoneNumber = StringSanitizer.sanitize(request.getPhoneNumber());
        String address = StringSanitizer.sanitize(request.getAddress());
        String city = StringSanitizer.sanitize(request.getCity());
        String state = StringSanitizer.sanitize(request.getState());
        String zipCode = StringSanitizer.sanitize(request.getZipCode());
        
        var facility = HealthcareFacility.builder()
                .name(name)
                .facilityType(request.getFacilityType())
                .cnpj(cnpj)
                .phoneNumber(phoneNumber)
                .address(address)
                .city(city)
                .state(state)
                .zipCode(zipCode)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .maxDailyCapacity(request.getMaxDailyCapacity())
                .currentLoad(0)
                .build();

        log.info("Unidade de saúde criada com sucesso");
        return facilityRepository.save(facility);
    }

    @Transactional
    public DoctorSchedule createDoctorSchedule(DoctorScheduleRequest request) {
        log.info("Criando agenda para o médico {} na unidade {}", request.getDoctorId(), request.getFacilityId());
        
        // Verificar se o médico existe
        peopleClient.doctorExists(request.getDoctorId());
        
        // Verificar se a unidade de saúde existe
        facilityRepository.findById(request.getFacilityId())
                .orElseThrow(() -> new EntityNotFoundException("Unidade de saúde com ID " + request.getFacilityId() + " não encontrada"));
        
        // Verificar se há sobreposição de horários para o médico no mesmo dia da semana
        List<DoctorSchedule> existingSchedules = scheduleRepository.findByDoctorIdAndDayOfWeek(
                request.getDoctorId(), request.getDayOfWeek());
        
        DoctorSchedule newSchedule = DoctorSchedule.builder()
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .secondPeriodStart(request.getSecondPeriodStart())
                .secondPeriodEnd(request.getSecondPeriodEnd())
                .doctorId(request.getDoctorId())
                .facilityId(request.getFacilityId())
                .build();
        
        for (DoctorSchedule existingSchedule : existingSchedules) {
            if (ScheduleTimeUtil.hasScheduleOverlap(newSchedule, existingSchedule)) {
                throw new DataIntegrityViolationException("Há sobreposição de horários com uma agenda existente do médico no mesmo dia da semana");
            }
        }
        
        log.info("Agenda criada com sucesso");
        return scheduleRepository.save(newSchedule);
    }

    @Transactional
    public AdministratorFacility assignAdministratorToFacility(AdministratorFacilityRequest request) {
        log.info("Associando administrador {} à unidade {}", request.getAdministratorId(), request.getHealthcareFacilityId());
        
        // Verificar se o administrador existe
        peopleClient.administratorExists(request.getAdministratorId());
        
        // Verificar se a unidade de saúde existe
        facilityRepository.findById(request.getHealthcareFacilityId())
                .orElseThrow(() -> new EntityNotFoundException("Unidade de saúde com ID " + request.getHealthcareFacilityId() + " não encontrada"));
        
        // Verificar se o administrador já está associado à unidade
        if (adminFacilityRepository.existsByAdministratorIdAndHealthcareFacilityId(
                request.getAdministratorId(), request.getHealthcareFacilityId())) {
            throw new DataIntegrityViolationException("Administrador já está associado a esta unidade de saúde");
        }
        
        var adminFacility = AdministratorFacility.builder()
                .administratorId(request.getAdministratorId())
                .healthcareFacilityId(request.getHealthcareFacilityId())
                .build();

        log.info("Administrador associado com sucesso");
        return adminFacilityRepository.save(adminFacility);
    }

    @Transactional
    public void handleAppointmentCreated(AppointmentCreatedEvent event) {
        log.info("Processando evento de agendamento criado: {}", event);
        
        try {
            var facility = facilityRepository.findById(event.getHealthcareFacilityId())
                    .orElseThrow(() -> new EntityNotFoundException("Facility not found with ID: " + event.getHealthcareFacilityId()));

            facility.setCurrentLoad(facility.getCurrentLoad() + 1);
            facilityRepository.save(facility);
            log.info("Carga da unidade {} atualizada para {}", facility.getId(), facility.getCurrentLoad());
        } catch (EntityNotFoundException e) {
            log.error("Erro ao processar evento de agendamento criado: {}", e.getMessage());
            // Aqui poderíamos implementar um mecanismo de DLQ (Dead Letter Queue) para tratar falhas
            throw e; // Re-lançar para que o mecanismo de retry do Kafka possa tentar novamente
        } catch (Exception e) {
            log.error("Erro inesperado ao processar evento de agendamento criado: {}", e.getMessage(), e);
            // Aqui poderíamos implementar um mecanismo de DLQ (Dead Letter Queue) para tratar falhas
            throw e; // Re-lançar para que o mecanismo de retry do Kafka possa tentar novamente
        }
    }

    public HealthcareFacility getFacility(Long id) {
        return facilityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Facility not found"));
    }

    public List<HealthcareFacility> getAvailableFacilities() {
        return facilityRepository.findAvailableFacilities();
    }

    public List<DoctorSchedule> getDoctorSchedules(Long doctorId) {
        log.info("Buscando agendas do médico {}", doctorId);
        
        // Verificar se o médico existe
        peopleClient.doctorExists(doctorId);
        
        List<DoctorSchedule> schedules = scheduleRepository.findByDoctorId(doctorId);
        log.info("Encontradas {} agendas para o médico {}", schedules.size(), doctorId);
        return schedules;
    }

    public List<DoctorSchedule> getFacilitySchedules(Long facilityId) {
        log.info("Buscando agendas da unidade {}", facilityId);
        
        // Verificar se a unidade existe
        facilityRepository.findById(facilityId)
                .orElseThrow(() -> new EntityNotFoundException("Unidade de saúde com ID " + facilityId + " não encontrada"));
        
        List<DoctorSchedule> schedules = scheduleRepository.findByFacilityId(facilityId);
        log.info("Encontradas {} agendas para a unidade {}", schedules.size(), facilityId);
        return schedules; // Retorna lista vazia se não houver agendas
    }

    public List<AdministratorFacility> getAdministratorFacilities(Long administratorId) {
        log.info("Buscando unidades do administrador {}", administratorId);
        
        // Verificar se o administrador existe
        peopleClient.administratorExists(administratorId);
        
        List<AdministratorFacility> facilities = adminFacilityRepository.findByAdministratorId(administratorId);
        log.info("Encontradas {} unidades para o administrador {}", facilities.size(), administratorId);
        return facilities;
    }

    public List<AdministratorFacility> getFacilityAdministrators(Long facilityId) {
        log.info("Buscando administradores da unidade {}", facilityId);
        
        // Verificar se a unidade existe
        facilityRepository.findById(facilityId)
                .orElseThrow(() -> new EntityNotFoundException("Unidade de saúde com ID " + facilityId + " não encontrada"));
        
        List<AdministratorFacility> administrators = adminFacilityRepository.findByHealthcareFacilityId(facilityId);
        log.info("Encontrados {} administradores para a unidade {}", administrators.size(), facilityId);
        return administrators; // Retorna lista vazia se não houver administradores
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
