package br.com.fiap.tech.people.repository;

import br.com.fiap.tech.people.domain.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    Optional<Administrator> findByCpf(String cpf);
}
