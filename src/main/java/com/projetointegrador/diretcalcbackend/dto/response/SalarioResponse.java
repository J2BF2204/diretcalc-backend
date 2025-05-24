package com.projetointegrador.diretcalcbackend.dto.response;

import com.projetointegrador.diretcalcbackend.model.Salario;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record SalarioResponse(

        Long id,
        LocalDate vigencia,
        String lei,
        BigDecimal valor

) {
    public static SalarioResponse fromEntity(Salario vi) {
        return SalarioResponse.builder()
                .id(vi.getId())
                .vigencia(vi.getVigencia())
                .lei(vi.getLei())
                .valor(vi.getValor())
                .build();
    }
}

