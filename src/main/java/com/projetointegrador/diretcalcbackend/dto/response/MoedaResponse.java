package com.projetointegrador.diretcalcbackend.dto.response;

import com.projetointegrador.diretcalcbackend.model.Moeda;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Builder
public record MoedaResponse(

        Long id,
        String nome,
        LocalDate vigencia,
        String sigla,
        String simbolo

) {
    public static MoedaResponse fromEntity(Moeda moeda) {
        return MoedaResponse.builder()
                .id(moeda.getId())
                .nome(moeda.getNome())
                .vigencia(moeda.getVigencia())
                .sigla(moeda.getSigla())
                .simbolo(moeda.getSimbolo())
                .build();
    }
}

