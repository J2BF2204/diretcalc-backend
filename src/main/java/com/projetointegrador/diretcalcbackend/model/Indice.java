package com.projetointegrador.diretcalcbackend.model;

import com.projetointegrador.diretcalcbackend.dto.request.IndiceRequest;
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
@Table(name = "tb_indices")
public class Indice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate vigencia;

    @Column(precision = 30, scale = 15)
    private BigDecimal indiceMonetario; // 10.000,0000

    @Column(precision = 30, scale = 15)
    private BigDecimal indiceIpcae; // 1,00000

    @Column(precision = 30, scale = 15)
    private BigDecimal SelicMensal;   // Ex: 3,63

    @Column(precision = 30, scale = 15)
    private BigDecimal SelicAcumulada;     // Ex: 10,49

    public Indice(IndiceRequest request) {
        this.vigencia = request.vigencia();
        this.indiceMonetario = request.indiceMonetario();
        this.indiceIpcae = request.indiceIpcae();
        this.SelicMensal = request.selicMensal();
        this.SelicAcumulada = request.selicAcumulada();
    }
}
