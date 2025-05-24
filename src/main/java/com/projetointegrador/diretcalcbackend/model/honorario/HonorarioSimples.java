package com.projetointegrador.diretcalcbackend.model.honorario;

import com.projetointegrador.diretcalcbackend.dto.request.HonorarioSimplesRequest;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HonorarioSimples extends Honorario {
    private BigDecimal percentual;

    public HonorarioSimples(HonorarioSimplesRequest request) {
        super();
        this.setValorBase(request.valorBase());
        this.setDataInicial(request.dataInicial());
        this.setDataFinal(request.dataFinal());
        this.setApuracao(request.apuracao());
        this.setAtualizacao(request.atualizacao());
        this.setPagamentoParcial(request.pagamentoParcial());
        this.setPercentual(request.percentual());
    }
}
