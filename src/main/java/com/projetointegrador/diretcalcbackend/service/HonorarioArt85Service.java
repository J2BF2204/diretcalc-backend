package com.projetointegrador.diretcalcbackend.service;

import com.projetointegrador.diretcalcbackend.dto.request.HonorarioArt85Request;
import com.projetointegrador.diretcalcbackend.dto.response.HonorarioArt85Response;
import com.projetointegrador.diretcalcbackend.mapper.HonorarioArt85Mapper;
import com.projetointegrador.diretcalcbackend.model.honorario.HonorarioArt85;
import com.projetointegrador.diretcalcbackend.repository.honorario.HonorarioArt85Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HonorarioArt85Service {
    private final HonorarioArt85Repository repository;
    private final HonorarioArt85Mapper mapper;

    public HonorarioArt85Service(
            HonorarioArt85Repository repository,
            HonorarioArt85Mapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<HonorarioArt85Response> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public HonorarioArt85Response buscarPorId(Long id) {
        HonorarioArt85 honorario = buscarHonorarioArt85(id);
        return mapper.toResponse(honorario);
    }

    public HonorarioArt85Response cadastrar(HonorarioArt85Request request) {
        HonorarioArt85 entity = mapper.toEntity(request);
        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    public Optional<HonorarioArt85Response> atualizar(Long id, HonorarioArt85Request request) {
        HonorarioArt85 honorario = buscarHonorarioArt85(id);

        honorario.setValorBase(request.valorBase());
        honorario.setDataInicial(request.dataInicial());
        honorario.setDataFinal(request.dataFinal());
        honorario.setApuracao(request.apuracao());
        honorario.setAtualizacao(request.atualizacao());
        honorario.setPagamentoParcial(request.pagamentoParcial());
        honorario.setFixacaoVerba(request.fixacaoVerba());
        honorario.setAliquota(request.aliquota());

        return Optional.of(mapper.toResponse(repository.save(honorario)));
    }

    public Optional<HonorarioArt85Response> atualizarAtributo(Long id, HonorarioArt85Request request) {
        HonorarioArt85 honorario = buscarHonorarioArt85(id);
        mapper.atualizarEntity(honorario, request);
        return Optional.of(mapper.toResponse(repository.save(honorario)));
    }

    public void deletar(Long id) {
        HonorarioArt85 honorario = buscarHonorarioArt85(id);
        repository.delete(honorario);
    }

    private HonorarioArt85 buscarHonorarioArt85(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Honorário com ID " + id + " não localizado"));
    }
}
