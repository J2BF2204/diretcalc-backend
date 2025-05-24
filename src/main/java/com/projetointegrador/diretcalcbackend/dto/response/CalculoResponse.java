package com.projetointegrador.diretcalcbackend.dto.response;

import com.projetointegrador.diretcalcbackend.model.Indice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record CalculoResponse(
        BigDecimal fatorAtualizacao,
        BigDecimal valorCorrigido,
        BigDecimal valorHonorario,
        BigDecimal totalCorrigidoComHonorarios,
        BigDecimal totalComDeducoes

) {


}

