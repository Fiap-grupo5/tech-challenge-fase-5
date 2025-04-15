package br.com.fiap.tech.identity.repository;

import br.com.fiap.tech.identity.domain.UserCpf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCpfRepository extends JpaRepository<UserCpf, Long> {
    boolean existsByCpf(String cpf);
    Optional<UserCpf> findByCpf(String cpf);
} 