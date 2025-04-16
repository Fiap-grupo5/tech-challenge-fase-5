package br.com.fiap.tech.people.service;

import br.com.fiap.tech.people.domain.Administrator;
import br.com.fiap.tech.people.domain.Doctor;
import br.com.fiap.tech.people.domain.Patient;
import br.com.fiap.tech.people.dto.AdministratorRequest;
import br.com.fiap.tech.people.dto.DoctorRequest;
import br.com.fiap.tech.people.dto.PatientRequest;
import br.com.fiap.tech.people.dto.UpdateAdministratorRequest;
import br.com.fiap.tech.people.dto.UpdateDoctorRequest;
import br.com.fiap.tech.people.dto.UpdatePatientRequest;
import br.com.fiap.tech.people.events.UserCreatedEvent;
import br.com.fiap.tech.people.events.UserDeletionEvent;
import br.com.fiap.tech.people.repository.AdministratorRepository;
import br.com.fiap.tech.people.repository.DoctorRepository;
import br.com.fiap.tech.people.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.util.StringUtils;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

@Slf4j
@Service
@RequiredArgsConstructor
public class PeopleService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AdministratorRepository administratorRepository;

    @EventListener
    public void handleUserCreated(UserCreatedEvent event) {
        log.info("Handling user created event for userId={}, userType={}", event.getUserId(), event.getUserType());
        try {
            switch (event.getUserType()) {
                case "PATIENT" -> {
                    log.debug("Creating patient from event: userId={}", event.getUserId());
                    createPatient(event);
                }
                case "DOCTOR" -> {
                    log.debug("Creating doctor from event: userId={}", event.getUserId());
                    createDoctor(event);
                }
                case "ADMINISTRATOR" -> {
                    log.debug("Creating administrator from event: userId={}", event.getUserId());
                    createAdministrator(event);
                }
                default -> {
                    log.warn("Unknown user type in event: {}", event.getUserType());
                }
            }
        } catch (Exception e) {
            log.error("Error creating user: userId={}, userType={}, error={}", 
                    event.getUserId(), event.getUserType(), e.getMessage(), e);
            throw new RuntimeException("Failed to process user creation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void createPatient(UserCreatedEvent event) {
        if (event.getUserId() == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (event.getFullName() == null || event.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (event.getCpf() == null || event.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF is required");
        }
        if (event.getEmail() == null || event.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (event.getNationalHealthCard() == null || event.getNationalHealthCard().trim().isEmpty()) {
            log.warn("National Health Card is missing for patient: {}", event.getUserId());
            // We don't throw an exception to maintain compatibility
        }

        String cpf = event.getCpf();
        if (cpf.length() > 11) {
            cpf = cpf.substring(0, 11);
        }

        String nationalHealthCard = event.getNationalHealthCard();
        if (nationalHealthCard != null && nationalHealthCard.length() > 20) {
            nationalHealthCard = nationalHealthCard.substring(0, 20);
        }

        String phoneNumber = event.getPhoneNumber();
        if (phoneNumber != null && phoneNumber.length() > 20) {
            phoneNumber = phoneNumber.substring(0, 20);
        }

        String state = event.getState();
        if (state != null && state.length() > 2) {
            state = state.substring(0, 2);
        }

        String zipCode = event.getZipCode();
        if (zipCode != null && zipCode.length() > 8) {
            zipCode = zipCode.substring(0, 8);
        }

        Patient patient = Patient.builder()
                .userId(event.getUserId())
                .fullName(event.getFullName())
                .cpf(cpf)
                .nationalHealthCard(nationalHealthCard)
                .email(event.getEmail())
                .birthDate(event.getBirthDate())
                .phoneNumber(phoneNumber)
                .address(event.getAddress())
                .city(event.getCity())
                .state(state)
                .zipCode(zipCode)
                .build();
        patient = patientRepository.save(patient);
        log.info("Patient created with ID: {}", patient.getId());
    }

    @Transactional
    public void createDoctor(UserCreatedEvent event) {
        if (event.getUserId() == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (event.getFullName() == null || event.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (event.getCpf() == null || event.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF is required");
        }
        if (event.getEmail() == null || event.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        String crm = event.getCrm();
        if (crm == null || crm.trim().isEmpty()) {
            log.warn("CRM is missing for doctor: {}", event.getUserId());
            crm = "PENDENTE";
        }
        
        String specialty = event.getSpecialty();
        if (specialty == null || specialty.trim().isEmpty()) {
            log.warn("Specialty is missing for doctor: {}", event.getUserId());
            specialty = "GERAL";
        }

        String cpf = event.getCpf();
        if (cpf.length() > 11) {
            cpf = cpf.substring(0, 11);
        }

        String phoneNumber = event.getPhoneNumber();
        if (phoneNumber != null && phoneNumber.length() > 20) {
            phoneNumber = phoneNumber.substring(0, 20);
        }

        Doctor doctor = Doctor.builder()
                .userId(event.getUserId())
                .fullName(event.getFullName())
                .cpf(cpf)
                .crm(crm)
                .specialty(specialty)
                .email(event.getEmail())
                .phoneNumber(phoneNumber)
                .build();
        doctor = doctorRepository.save(doctor);
        log.info("Doctor created with ID: {}", doctor.getId());
    }

    @Transactional
    public void createAdministrator(UserCreatedEvent event) {
        if (event.getUserId() == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (event.getFullName() == null || event.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (event.getCpf() == null || event.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF is required");
        }
        if (event.getEmail() == null || event.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        String cpf = event.getCpf();
        if (cpf.length() > 11) {
            cpf = cpf.substring(0, 11);
        }

        String phoneNumber = event.getPhoneNumber();
        if (phoneNumber != null && phoneNumber.length() > 20) {
            phoneNumber = phoneNumber.substring(0, 20);
        }

        Administrator administrator = Administrator.builder()
                .userId(event.getUserId())
                .fullName(event.getFullName())
                .cpf(cpf)
                .email(event.getEmail())
                .phoneNumber(phoneNumber)
                .build();
        administrator = administratorRepository.save(administrator);
        log.info("Administrator created with ID: {}", administrator.getId());
    }

    @Transactional
    public void updatePatient(UpdatePatientRequest request) {
        Patient patient = patientRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            patient.setFullName(request.getFullName());
        }
        if (request.getCpf() != null && !request.getCpf().trim().isEmpty()) {
            String cpf = request.getCpf();
            if (cpf.length() > 11) {
                cpf = cpf.substring(0, 11);
            }
            patient.setCpf(cpf);
        }
        if (request.getNationalHealthCard() != null && !request.getNationalHealthCard().trim().isEmpty()) {
            String nationalHealthCard = request.getNationalHealthCard();
            if (nationalHealthCard.length() > 20) {
                nationalHealthCard = nationalHealthCard.substring(0, 20);
            }
            patient.setNationalHealthCard(nationalHealthCard);
        }
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            patient.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            String phoneNumber = request.getPhoneNumber();
            if (phoneNumber.length() > 20) {
                phoneNumber = phoneNumber.substring(0, 20);
            }
            patient.setPhoneNumber(phoneNumber);
        }
        if (request.getBirthDate() != null) {
            patient.setBirthDate(request.getBirthDate());
        }
        if (request.getAddress() != null) {
            patient.setAddress(request.getAddress());
        }
        if (request.getCity() != null) {
            patient.setCity(request.getCity());
        }
        if (request.getState() != null) {
            String state = request.getState();
            if (state.length() > 2) {
                state = state.substring(0, 2);
            }
            patient.setState(state);
        }
        if (request.getZipCode() != null) {
            String zipCode = request.getZipCode();
            if (zipCode.length() > 8) {
                zipCode = zipCode.substring(0, 8);
            }
            patient.setZipCode(zipCode);
        }

        patientRepository.save(patient);
    }

    @Transactional
    public void updateDoctor(UpdateDoctorRequest request) {
        var doctor = doctorRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            doctor.setFullName(request.getFullName());
        }
        if (request.getCpf() != null && !request.getCpf().trim().isEmpty()) {
            // Ensure CPF has at most 11 characters
            String cpf = request.getCpf();
            if (cpf.length() > 11) {
                cpf = cpf.substring(0, 11);
            }
            doctor.setCpf(cpf);
        }
        if (request.getCrm() != null && !request.getCrm().trim().isEmpty()) {
            doctor.setCrm(request.getCrm());
        }
        if (request.getSpecialty() != null && !request.getSpecialty().trim().isEmpty()) {
            doctor.setSpecialty(request.getSpecialty());
        }
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            doctor.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            // Ensure phone number has at most 20 characters
            String phoneNumber = request.getPhoneNumber();
            if (phoneNumber.length() > 20) {
                phoneNumber = phoneNumber.substring(0, 20);
            }
            doctor.setPhoneNumber(phoneNumber);
        }

        doctorRepository.save(doctor);
    }

    @Transactional
    public void updateAdministrator(UpdateAdministratorRequest request) {
        var administrator = administratorRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Administrator not found"));

        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            administrator.setFullName(request.getFullName());
        }
        if (request.getCpf() != null && !request.getCpf().trim().isEmpty()) {
            String cpf = request.getCpf();
            if (cpf.length() > 11) {
                cpf = cpf.substring(0, 11);
            }
            administrator.setCpf(cpf);
        }
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            administrator.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            // Ensure phone number has at most 20 characters
            String phoneNumber = request.getPhoneNumber();
            if (phoneNumber.length() > 20) {
                phoneNumber = phoneNumber.substring(0, 20);
            }
            administrator.setPhoneNumber(phoneNumber);
        }

        administratorRepository.save(administrator);
    }

    public Patient getPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
    }

    public Doctor getDoctor(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
    }

    public Administrator getAdministrator(Long id) {
        return administratorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Administrator not found"));
    }

    @Transactional
    public void handleUserDeleted(UserDeletionEvent event) {
        log.info("Processing user deletion event: userId={}", event.getUserId());

        var userId = event.getUserId();
        if (userId == null) {
            log.error("Error processing deletion: User ID is null");
            throw new IllegalArgumentException("User ID cannot be null");
        }

        boolean removed = false;

        try {
            if (removeFromPatient(userId)) {
                removed = true;
            }

            if (removeFromDoctor(userId)) {
                removed = true;
            }

            if (removeFromAdmin(userId)) {
                removed = true;
            }

            if (removed) {
                log.info("User successfully removed from at least one table: userId={}", userId);
            } else {
                log.warn("User not found in any table: userId={}", userId);
            }
        } catch (Exception e) {
            log.error("Error during user removal: userId={}, error={}", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to process user deletion: " + e.getMessage(), e);
        }
    }

    private boolean removeFromPatient(Long userId) {
        try {
            log.debug("Attempting to remove patient with userId={}", userId);
            return patientRepository.findByUserId(userId)
                    .map(patient -> {
                        log.info("Removing patient: userId={}, patientId={}", userId, patient.getId());
                        patientRepository.delete(patient);
                        return true;
                    })
                    .orElseGet(() -> {
                        log.debug("No patient found for userId={}", userId);
                        return false;
                    });
        } catch (Exception e) {
            log.error("Error removing patient: userId={}, error={}", userId, e.getMessage(), e);
            return false;
        }
    }

    private boolean removeFromDoctor(Long userId) {
        try {
            log.debug("Attempting to remove doctor with userId={}", userId);
            return doctorRepository.findByUserId(userId)
                    .map(doctor -> {
                        log.info("Removing doctor: userId={}, doctorId={}, crm={}", 
                                userId, doctor.getId(), doctor.getCrm());
                        doctorRepository.delete(doctor);
                        return true;
                    })
                    .orElseGet(() -> {
                        log.debug("No doctor found for userId={}", userId);
                        return false;
                    });
        } catch (Exception e) {
            log.error("Error removing doctor: userId={}, error={}", userId, e.getMessage(), e);
            return false;
        }
    }

    private boolean removeFromAdmin(Long userId) {
        try {
            log.debug("Attempting to remove administrator with userId={}", userId);
            return administratorRepository.findByUserId(userId)
                    .map(admin -> {
                        log.info("Removing administrator: userId={}, adminId={}", userId, admin.getId());
                        administratorRepository.delete(admin);
                        return true;
                    })
                    .orElseGet(() -> {
                        log.debug("No administrator found for userId={}", userId);
                        return false;
                    });
        } catch (Exception e) {
            log.error("Error removing administrator: userId={}, error={}", userId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Verifica se um CPF já existe em qualquer uma das tabelas
     *
     * @param cpf O CPF a ser verificado
     * @return true se o CPF existe, false caso contrário
     */
    public boolean checkCpfExists(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }
        
        String normalizedCpf = normalizeCpf(cpf);
        
        log.info("Checking if CPF exists: {}", normalizedCpf);
        
        try {
            boolean patientExists = checkCpfExistsInPatients(normalizedCpf);
            if (patientExists) {
                log.info("CPF {} exists in the patients table", normalizedCpf);
                return true;
            }
            
            boolean doctorExists = checkCpfExistsInDoctors(normalizedCpf);
            if (doctorExists) {
                log.info("CPF {} exists in the doctors table", normalizedCpf);
                return true;
            }
            
            boolean adminExists = checkCpfExistsInAdmins(normalizedCpf);
            if (adminExists) {
                log.info("CPF {} exists in the administrators table", normalizedCpf);
                return true;
            }
            
            log.info("CPF {} does not exist in any table.", normalizedCpf);
            return false;
        } catch (Exception e) {
            log.error("Error while verifying CPF: {}", e.getMessage(), e);
            return true;
        }
    }
    
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 500))
    private boolean checkCpfExistsInPatients(String cpf) {
        return patientRepository.findByCpf(cpf).isPresent();
    }
    
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 500))
    private boolean checkCpfExistsInDoctors(String cpf) {
        return doctorRepository.findByCpf(cpf).isPresent();
    }
    
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 500))
    private boolean checkCpfExistsInAdmins(String cpf) {
        return administratorRepository.findByCpf(cpf).isPresent();
    }


    private String normalizeCpf(String cpf) {
        if (cpf == null) {
            return "";
        }
        String normalized = cpf.replaceAll("[^0-9]", "");
        if (normalized.length() > 11) {
            normalized = normalized.substring(0, 11);
        }
        return normalized;
    }

    public boolean checkCrmExists(String crm) {
        if (crm == null || crm.trim().isEmpty()) {
            return false;
        }
        
        log.info("Checking if CRM exists: {}", crm);
        
        try {
            boolean exists = doctorRepository.findByCrm(crm).isPresent();
            log.info("CRM {} exists: {}", crm, exists);
            return exists;
        } catch (Exception e) {
            log.error("Error while verifying CRM: {}", e.getMessage(), e);
            return true;
        }
    }
}
