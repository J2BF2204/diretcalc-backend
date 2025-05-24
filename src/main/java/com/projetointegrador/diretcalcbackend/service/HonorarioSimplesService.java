package com.projetointegrador.diretcalcbackend.service;

import com.projetointegrador.diretcalcbackend.dto.request.HonorarioSimplesRequest;
import com.projetointegrador.diretcalcbackend.dto.response.HonorarioSimplesResponse;
import com.projetointegrador.diretcalcbackend.mapper.HonorarioSimplesMapper;
import com.projetointegrador.diretcalcbackend.model.honorario.HonorarioSimples;
import com.projetointegrador.diretcalcbackend.repository.honorario.HonorarioSimplesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HonorarioSimplesService {
    private final HonorarioSimplesRepository repository;
    private final HonorarioSimplesMapper mapper;

    public HonorarioSimplesService(
            HonorarioSimplesRepository repository,
            HonorarioSimplesMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<HonorarioSimplesResponse> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public HonorarioSimplesResponse buscarPorId(Long id) {
        HonorarioSimples honorario = buscarHonorarioSimples(id);
        return mapper.toResponse(honorario);
    }

    public HonorarioSimplesResponse cadastrar(HonorarioSimplesRequest request) {
        HonorarioSimples entity = mapper.toEntity(request);
        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    public Optional<HonorarioSimplesResponse> atualizar(Long id, HonorarioSimplesRequest request) {
        HonorarioSimples honorario = buscarHonorarioSimples(id);

        honorario.setValorBase(request.valorBase());
        honorario.setDataInicial(request.dataInicial());
        honorario.setDataFinal(request.dataFinal());
        honorario.setApuracao(request.apuracao());
        honorario.setAtualizacao(request.atualizacao());
        honorario.setPagamentoParcial(request.pagamentoParcial());
        honorario.setPercentual(request.percentual());

        return Optional.of(mapper.toResponse(repository.save(honorario)));
    }

    public Optional<HonorarioSimplesResponse> atualizarAtributo(Long id, HonorarioSimplesRequest request) {
        HonorarioSimples honorario = buscarHonorarioSimples(id);
        mapper.atualizarEntity(honorario, request);
        return Optional.of(mapper.toResponse(repository.save(honorario)));
    }

    public void deletar(Long id) {
        HonorarioSimples honorario = buscarHonorarioSimples(id);
        repository.delete(honorario);
    }

    private HonorarioSimples buscarHonorarioSimples(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Honorário com ID " + id + " não localizado"));
    }
}
