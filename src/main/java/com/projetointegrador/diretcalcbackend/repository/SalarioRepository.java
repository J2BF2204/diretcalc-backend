package com.projetointegrador.diretcalcbackend.repository;

import com.projetointegrador.diretcalcbackend.model.Salario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SalarioRepository extends JpaRepository<Salario, Long> {

    Optional<Salario> findFirstByVigenciaLessThanEqualOrderByVigenciaDesc(LocalDate data);


}

