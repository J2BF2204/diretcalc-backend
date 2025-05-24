package com.projetointegrador.diretcalcbackend.model;

import com.projetointegrador.diretcalcbackend.dto.request.SalarioRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_salarios")
public class Salario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate vigencia; // out/64 ⇒ 1964-10

    private String lei;

    @Column(precision = 30, scale = 15)
    private BigDecimal valor; // 1,00000

    public Salario(SalarioRequest request) {
        this.vigencia = request.vigencia();
        this.lei = request.lei();
        this.valor = request.valor();
    }

}
