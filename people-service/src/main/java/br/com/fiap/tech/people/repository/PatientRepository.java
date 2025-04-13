package br.com.fiap.tech.people.repository;

import br.com.fiap.tech.people.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByCpf(String cpf);
    Optional<Patient> findByNationalHealthCard(String nationalHealthCard);
    Optional<Patient> findByUserId(Long userId);
}
