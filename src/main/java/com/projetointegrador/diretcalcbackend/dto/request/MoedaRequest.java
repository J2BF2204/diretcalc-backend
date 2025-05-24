package com.projetointegrador.diretcalcbackend.dto.request;

import java.time.LocalDate;

public record MoedaRequest(
        String nome,
        LocalDate vigencia,

        String sigla,
        String simbolo
) {
}

