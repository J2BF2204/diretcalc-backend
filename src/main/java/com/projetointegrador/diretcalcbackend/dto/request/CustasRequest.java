package com.projetointegrador.diretcalcbackend.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CustasRequest(
        BigDecimal valor,
        LocalDate dataOcorrencia
) {
}

