package com.projetointegrador.diretcalcbackend.model;

import com.projetointegrador.diretcalcbackend.enums.Apuracao;
import com.projetointegrador.diretcalcbackend.enums.Atualizacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Custas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal valorBase;
    private LocalDate dataOcorrencia;

}
