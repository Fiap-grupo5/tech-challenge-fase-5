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
import br.com.fiap.tech.people.repository.AdministratorRepository;
import br.com.fiap.tech.people.repository.DoctorRepository;
import br.com.fiap.tech.people.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.event.EventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class PeopleService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AdministratorRepository administratorRepository;

    @EventListener
    public void handleUserCreated(UserCreatedEvent event) {
        switch (event.getUserType()) {
            case "PATIENT" -> createPatient(event);
            case "DOCTOR" -> createDoctor(event);
            case "ADMINISTRATOR" -> createAdministrator(event);
            default -> log.warn("Unknown user type: {}", event.getUserType());
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
        if (event.getNationalHealthCard() == null || event.getNationalHealthCard().trim().isEmpty()) {
            throw new IllegalArgumentException("National Health Card is required for patients");
        }

        String cpf = event.getCpf();
        if (cpf.length() > 11) {
            cpf = cpf.substring(0, 11);
        }

        String nationalHealthCard = event.getNationalHealthCard();
        if (nationalHealthCard.length() > 20) {
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
                .birthDate(event.getBirthDate())
                .phoneNumber(phoneNumber)
                .address(event.getAddress())
                .city(event.getCity())
                .state(state)
                .zipCode(zipCode)
                .build();
        patient = patientRepository.save(patient);
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
        if (event.getCrm() == null || event.getCrm().trim().isEmpty()) {
            throw new IllegalArgumentException("CRM is required for doctors");
        }
        if (event.getSpecialty() == null || event.getSpecialty().trim().isEmpty()) {
            throw new IllegalArgumentException("Specialty is required for doctors");
        }

        String cpf = event.getCpf();
        if (cpf.length() > 11) {
            cpf = cpf.substring(0, 11);
        }

        Doctor doctor = Doctor.builder()
                .userId(event.getUserId())
                .fullName(event.getFullName())
                .cpf(cpf)
                .crm(event.getCrm())
                .specialty(event.getSpecialty())
                .phoneNumber(event.getPhoneNumber())
                .build();
        doctor = doctorRepository.save(doctor);
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

        String cpf = event.getCpf();
        if (cpf.length() > 11) {
            cpf = cpf.substring(0, 11);
        }

        Administrator administrator = Administrator.builder()
                .userId(event.getUserId())
                .fullName(event.getFullName())
                .cpf(cpf)
                .phoneNumber(event.getPhoneNumber())
                .build();
        administrator = administratorRepository.save(administrator);
    }

    @Transactional
    public void updatePatient(UpdatePatientRequest request) {
        Patient patient = patientRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            patient.setFullName(request.getFullName());
        }
        if (request.getCpf() != null && !request.getCpf().trim().isEmpty()) {
            // Garantir que o CPF tenha no máximo 11 caracteres
            String cpf = request.getCpf();
            if (cpf.length() > 11) {
                cpf = cpf.substring(0, 11);
            }
            patient.setCpf(cpf);
        }
        if (request.getNationalHealthCard() != null && !request.getNationalHealthCard().trim().isEmpty()) {
            // Garantir que o cartão nacional de saúde tenha no máximo 20 caracteres
            String nationalHealthCard = request.getNationalHealthCard();
            if (nationalHealthCard.length() > 20) {
                nationalHealthCard = nationalHealthCard.substring(0, 20);
            }
            patient.setNationalHealthCard(nationalHealthCard);
        }
        if (request.getPhoneNumber() != null) {
            // Garantir que o número de telefone tenha no máximo 20 caracteres
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
            // Garantir que o estado tenha no máximo 2 caracteres
            String state = request.getState();
            if (state.length() > 2) {
                state = state.substring(0, 2);
            }
            patient.setState(state);
        }
        if (request.getZipCode() != null) {
            // Garantir que o CEP tenha no máximo 8 caracteres
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
        Doctor doctor = doctorRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            doctor.setFullName(request.getFullName());
        }
        if (request.getCpf() != null && !request.getCpf().trim().isEmpty()) {
            // Garantir que o CPF tenha no máximo 11 caracteres
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
        if (request.getPhoneNumber() != null) {
            // Garantir que o número de telefone tenha no máximo 20 caracteres
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
        Administrator administrator = administratorRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Administrator not found"));

        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            administrator.setFullName(request.getFullName());
        }
        if (request.getCpf() != null && !request.getCpf().trim().isEmpty()) {
            // Garantir que o CPF tenha no máximo 11 caracteres
            String cpf = request.getCpf();
            if (cpf.length() > 11) {
                cpf = cpf.substring(0, 11);
            }
            administrator.setCpf(cpf);
        }
        if (request.getPhoneNumber() != null) {
            // Garantir que o número de telefone tenha no máximo 20 caracteres
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
}
