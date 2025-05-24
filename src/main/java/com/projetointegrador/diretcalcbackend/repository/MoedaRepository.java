package com.projetointegrador.diretcalcbackend.repository;

import com.projetointegrador.diretcalcbackend.model.Moeda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoedaRepository extends JpaRepository<Moeda, Long> {}

