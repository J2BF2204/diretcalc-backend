package com.projetointegrador.diretcalcbackend.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalarioRequest(
        LocalDate vigencia,
        String lei,
        BigDecimal valor

) {
}

