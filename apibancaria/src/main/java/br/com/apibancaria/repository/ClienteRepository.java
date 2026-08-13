package br.com.apibancaria.repository;

import br.com.apibancaria.model.Cliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    boolean existsByCpf(@NotBlank String cpf);

    boolean existsByEmail(@Email String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<Cliente> findById(Long id);

    Optional<Cliente> findByEmail(String email);
}
