package br.com.apibancaria.repository;

import br.com.apibancaria.model.ChavePix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChavePixRepository extends JpaRepository<ChavePix, Long> {
}
