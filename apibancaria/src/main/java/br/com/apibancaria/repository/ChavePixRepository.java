package br.com.apibancaria.repository;

import br.com.apibancaria.model.ChavePix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChavePixRepository extends JpaRepository<ChavePix, Long> {
    Optional<ChavePix> findByChaveAndAtivaTrue(String chave);

    Optional<ChavePix> findByChave(String chave);

    List<ChavePix> findByContaIdAndAtivaTrue(Long contaId);
}
