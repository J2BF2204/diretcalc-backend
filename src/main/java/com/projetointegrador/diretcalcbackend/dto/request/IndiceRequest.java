package com.projetointegrador.diretcalcbackend.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IndiceRequest(
        LocalDate vigencia,
        BigDecimal indiceMonetario,
        BigDecimal indiceIpcae,
        BigDecimal selicMensal,
        BigDecimal selicAcumulada
) {
}

