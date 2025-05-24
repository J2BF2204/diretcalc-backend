package com.projetointegrador.diretcalcbackend.repository;

import com.projetointegrador.diretcalcbackend.model.Indice;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface IndiceRepository extends JpaRepository<Indice, Long> {
    Optional<Indice> findByVigenciaBetween(LocalDate inicio, LocalDate fim);
}

