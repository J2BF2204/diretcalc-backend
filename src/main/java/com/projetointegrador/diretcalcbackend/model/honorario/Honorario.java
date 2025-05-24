package com.projetointegrador.diretcalcbackend.model.honorario;

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
public abstract class Honorario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal valorBase;
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private Apuracao apuracao;
    private Atualizacao atualizacao;
    private BigDecimal pagamentoParcial;

}
