package com.projetointegrador.diretcalcbackend.mapper;

import com.projetointegrador.diretcalcbackend.dto.request.HonorarioArt85Request;
import com.projetointegrador.diretcalcbackend.dto.response.HonorarioArt85Response;
import com.projetointegrador.diretcalcbackend.model.honorario.HonorarioArt85;
import org.springframework.stereotype.Component;


@Component
public class HonorarioArt85Mapper {

    public HonorarioArt85 toEntity(HonorarioArt85Request request) {
        HonorarioArt85 honorario = new HonorarioArt85();
        honorario.setValorBase(request.valorBase());
        honorario.setDataInicial(request.dataInicial());
        honorario.setDataFinal(request.dataFinal());
        honorario.setApuracao(request.apuracao());
        honorario.setAtualizacao(request.atualizacao());
        honorario.setPagamentoParcial(request.pagamentoParcial());
        honorario.setPagamentoParcial(request.pagamentoParcial());
        honorario.setAliquota(request.aliquota());
        return honorario;
    }

    public HonorarioArt85Response toResponse(HonorarioArt85 entity) {
        return new HonorarioArt85Response(
                entity.getId(),
                entity.getValorBase(),
                entity.getDataInicial(),
                entity.getDataFinal(),
                entity.getApuracao(),
                entity.getAtualizacao(),
                entity.getPagamentoParcial(),
                entity.getFixacaoVerba(),
                entity.getAliquota()
        );
    }

    public void atualizarEntity(HonorarioArt85 entity, HonorarioArt85Request request) {
        if (request.valorBase() != null) entity.setValorBase(request.valorBase());
        if (request.dataInicial() != null) entity.setDataInicial(request.dataInicial());
        if (request.dataFinal() != null) entity.setDataFinal(request.dataFinal());
        if (request.apuracao() != null) entity.setApuracao(request.apuracao());
        if (request.atualizacao() != null) entity.setAtualizacao(request.atualizacao());
        if (request.pagamentoParcial() != null) entity.setPagamentoParcial(request.pagamentoParcial());
        if (request.fixacaoVerba() != null) entity.setFixacaoVerba(request.fixacaoVerba());
        if (request.aliquota() != null) entity.setAliquota(request.aliquota());
    }

}
