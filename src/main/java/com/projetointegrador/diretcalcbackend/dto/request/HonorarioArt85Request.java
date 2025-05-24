package com.projetointegrador.diretcalcbackend.dto.request;

import com.projetointegrador.diretcalcbackend.enums.Aliquota;
import com.projetointegrador.diretcalcbackend.enums.Apuracao;
import com.projetointegrador.diretcalcbackend.enums.Atualizacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record HonorarioArt85Request(

        BigDecimal valorBase,
        LocalDate dataInicial,
        LocalDate dataFinal,
        Apuracao apuracao,
        Atualizacao atualizacao,
        BigDecimal pagamentoParcial,
        LocalDate fixacaoVerba,
        Aliquota aliquota
) {
}
