package br.com.fiap.tech.people.repository;

import br.com.fiap.tech.people.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByCrm(String crm);
    Optional<Doctor> findByCpf(String cpf);
}
