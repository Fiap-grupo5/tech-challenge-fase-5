package br.com.fiap.tech.people.service;

import br.com.fiap.tech.people.domain.Administrator;
import br.com.fiap.tech.people.domain.Doctor;
import br.com.fiap.tech.people.domain.Patient;
import br.com.fiap.tech.people.dto.AdministratorRequest;
import br.com.fiap.tech.people.dto.DoctorRequest;
import br.com.fiap.tech.people.dto.PatientRequest;
import br.com.fiap.tech.people.events.UserCreatedEvent;
import br.com.fiap.tech.people.repository.AdministratorRepository;
import br.com.fiap.tech.people.repository.DoctorRepository;
import br.com.fiap.tech.people.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PeopleService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AdministratorRepository administratorRepository;

    @Transactional
    public void handleUserCreated(UserCreatedEvent event) {
        switch (event.getUserType()) {
            case "PATIENT":
                Patient patient = Patient.builder()
                        .id(event.getUserId())
                        .build();
                patientRepository.save(patient);
                break;
            case "DOCTOR":
                Doctor doctor = Doctor.builder()
                        .id(event.getUserId())
                        .build();
                doctorRepository.save(doctor);
                break;
            case "ADMINISTRATOR":
                Administrator administrator = Administrator.builder()
                        .id(event.getUserId())
                        .build();
                administratorRepository.save(administrator);
                break;
        }
    }

    @Transactional
    public Patient updatePatient(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        patient.setFullName(request.getFullName());
        patient.setCpf(request.getCpf());
        patient.setNationalHealthCard(request.getNationalHealthCard());
        patient.setBirthDate(request.getBirthDate());
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setAddress(request.getAddress());
        patient.setCity(request.getCity());
        patient.setState(request.getState());
        patient.setZipCode(request.getZipCode());

        return patientRepository.save(patient);
    }

    @Transactional
    public Doctor updateDoctor(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        doctor.setFullName(request.getFullName());
        doctor.setCpf(request.getCpf());
        doctor.setCrm(request.getCrm());
        doctor.setSpecialty(request.getSpecialty());
        doctor.setPhoneNumber(request.getPhoneNumber());

        return doctorRepository.save(doctor);
    }

    @Transactional
    public Administrator updateAdministrator(Long id, AdministratorRequest request) {
        Administrator administrator = administratorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Administrator not found"));

        administrator.setFullName(request.getFullName());
        administrator.setCpf(request.getCpf());
        administrator.setPhoneNumber(request.getPhoneNumber());

        return administratorRepository.save(administrator);
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
