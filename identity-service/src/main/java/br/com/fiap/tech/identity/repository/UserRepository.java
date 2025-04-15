package br.com.fiap.tech.identity.repository;

import br.com.fiap.tech.identity.domain.User;
import br.com.fiap.tech.identity.domain.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    
    List<User> findByUserType(UserType userType);
}
