package com.projetointegrador.diretcalcbackend.dto.response;

import com.projetointegrador.diretcalcbackend.enums.Apuracao;
import com.projetointegrador.diretcalcbackend.enums.Atualizacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record HonorarioSimplesResponse(
        Long id,
        BigDecimal valorBase,
        LocalDate dataInicial,
        LocalDate dataFinal,
        Apuracao apuracao,
        Atualizacao atualizacao,
        BigDecimal pagamentoParcial,
        BigDecimal percentual
) {}

