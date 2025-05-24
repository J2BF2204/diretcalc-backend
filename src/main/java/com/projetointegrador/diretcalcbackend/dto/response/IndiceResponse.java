package com.projetointegrador.diretcalcbackend.dto.response;

import com.projetointegrador.diretcalcbackend.model.Indice;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record IndiceResponse(

        Long id,
        LocalDate vigencia,
        BigDecimal indiceMonetario,
        BigDecimal indiceIpcae,
        BigDecimal selicMensal,
        BigDecimal selicAcumulada

) {
    public static IndiceResponse fromEntity(Indice indice) {
        return IndiceResponse.builder()
                .id(indice.getId())
                .vigencia(indice.getVigencia()) // YYYY-MM
                .indiceMonetario(indice.getIndiceMonetario())
                .indiceIpcae(indice.getIndiceIpcae())
                .selicMensal(indice.getSelicMensal())
                .selicAcumulada(indice.getSelicAcumulada())
                .build();
    }
}

