package com.projetointegrador.diretcalcbackend.mapper;

import com.projetointegrador.diretcalcbackend.dto.request.HonorarioSimplesRequest;
import com.projetointegrador.diretcalcbackend.dto.response.HonorarioSimplesResponse;
import com.projetointegrador.diretcalcbackend.model.honorario.HonorarioSimples;
import org.springframework.stereotype.Component;


@Component
public class HonorarioSimplesMapper {

    public HonorarioSimples toEntity(HonorarioSimplesRequest request) {
        HonorarioSimples entity = new HonorarioSimples();
        entity.setValorBase(request.valorBase());
        entity.setDataInicial(request.dataInicial());
        entity.setDataFinal(request.dataFinal());
        entity.setApuracao(request.apuracao());
        entity.setAtualizacao(request.atualizacao());
        entity.setPagamentoParcial(request.pagamentoParcial());
        entity.setPagamentoParcial(request.pagamentoParcial());
        entity.setPercentual(request.percentual());
        return entity;
    }

    public HonorarioSimplesResponse toResponse(HonorarioSimples entity) {
        return new HonorarioSimplesResponse(
                entity.getId(),
                entity.getValorBase(),
                entity.getDataInicial(),
                entity.getDataFinal(),
                entity.getApuracao(),
                entity.getAtualizacao(),
                entity.getPagamentoParcial(),
                entity.getPercentual()
        );
    }

    public void atualizarEntity(HonorarioSimples entity, HonorarioSimplesRequest request) {
        if (request.valorBase() != null) entity.setValorBase(request.valorBase());
        if (request.dataInicial() != null) entity.setDataInicial(request.dataInicial());
        if (request.dataFinal() != null) entity.setDataFinal(request.dataFinal());
        if (request.apuracao() != null) entity.setApuracao(request.apuracao());
        if (request.atualizacao() != null) entity.setAtualizacao(request.atualizacao());
        if (request.pagamentoParcial() != null) entity.setPagamentoParcial(request.pagamentoParcial());
        if (request.percentual() != null) entity.setPercentual(request.percentual());
    }

}
