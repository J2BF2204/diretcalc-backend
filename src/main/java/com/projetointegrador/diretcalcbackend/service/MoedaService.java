package com.projetointegrador.diretcalcbackend.service;

import com.projetointegrador.diretcalcbackend.dto.request.MoedaRequest;
import com.projetointegrador.diretcalcbackend.dto.response.MoedaResponse;
import com.projetointegrador.diretcalcbackend.model.Moeda;
import com.projetointegrador.diretcalcbackend.repository.MoedaRepository;
import com.projetointegrador.diretcalcbackend.utils.ExcelParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MoedaService {

    private final MoedaRepository repository;

    public MoedaService(MoedaRepository repository) {
        this.repository = repository;
    }

    public List<MoedaResponse> importarExcel(InputStream inputStream) throws IOException {
        ExcelParser parser = new ExcelParser();
        List<Moeda> lista = parser.parseExcelMoeda(inputStream);
        List<Moeda> salvos = repository.saveAll(lista);
        return salvos.stream()
                .map(MoedaResponse::fromEntity)
                .collect(Collectors.toList());
    }


    public List<MoedaRequest> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::mapearParaDTO)
                .toList();
    }

    public MoedaRequest buscarPorId(Long id) {
        Moeda moeda = buscarMoeda(id);
        return mapearParaDTO(moeda);
    }

    public MoedaRequest cadastrar(MoedaRequest request) {
        Moeda moeda = new Moeda(request);
        return mapearParaDTO(repository.save(moeda));
    }

    public Optional<MoedaRequest> atualizar(Long id, MoedaRequest request) {
        Moeda moeda = buscarMoeda(id);

        moeda.setId(id);
        moeda.setVigencia(request.vigencia());
        moeda.setNome(request.nome());


        return Optional.of(mapearParaDTO(repository.save(moeda)));
    }

    public Optional<MoedaRequest> atualizarAtributo(Long id, MoedaRequest request) {
        Moeda moeda = buscarMoeda(id);
        atualizarDadosMoeda(moeda, request);

        return Optional.of(mapearParaDTO(repository.save(moeda)));
    }

    public void deletar(Long id) {
        Moeda moeda = buscarMoeda(id);
        repository.delete(moeda);
    }

    private Moeda buscarMoeda(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Índice com ID " + id + " não localizado"));
    }

    private MoedaRequest mapearParaDTO(Moeda moeda) {
        return new MoedaRequest(
                moeda.getNome(),
                moeda.getVigencia(),
                moeda.getSigla(),
                moeda.getSimbolo()
        );
    }

    private void atualizarDadosMoeda(Moeda moeda, MoedaRequest request) {
        if (request.nome() != null) moeda.setNome(request.nome());
        if (request.vigencia() != null) moeda.setVigencia(request.vigencia());
        if (request.sigla() != null) moeda.setSigla(request.sigla());
        if (request.simbolo() != null) moeda.setSimbolo(request.simbolo());
    }
}

