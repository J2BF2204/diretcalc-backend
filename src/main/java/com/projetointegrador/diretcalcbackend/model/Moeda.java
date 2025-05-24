package com.projetointegrador.diretcalcbackend.model;

import com.projetointegrador.diretcalcbackend.dto.request.MoedaRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_moedas")
public class Moeda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private LocalDate vigencia;

    private String sigla;
    private String simbolo;

    public Moeda(MoedaRequest request) {
        this.nome = request.nome();
        this.vigencia = request.vigencia();
        this.sigla = request.sigla();
        this.simbolo = request.simbolo();
    }
}

