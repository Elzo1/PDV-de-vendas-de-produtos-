package br.com.divMaster.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.divMaster.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // Nome do método corrigido para corresponder ao padrão do Spring Data JPA
     Optional<User> findByUsername(String username);
}
