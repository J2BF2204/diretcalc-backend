package com.projetointegrador.diretcalcbackend.service;

import com.projetointegrador.diretcalcbackend.dto.request.SalarioRequest;
import com.projetointegrador.diretcalcbackend.dto.response.SalarioResponse;
import com.projetointegrador.diretcalcbackend.model.Salario;
import com.projetointegrador.diretcalcbackend.repository.SalarioRepository;
import com.projetointegrador.diretcalcbackend.utils.ExcelParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SalarioService {

    private final SalarioRepository repository;

    public SalarioService(SalarioRepository repository) {
        this.repository = repository;
    }

    public List<SalarioResponse> importarExcel(InputStream inputStream) throws IOException {
        ExcelParser parser = new ExcelParser();
        List<Salario> lista = parser.parseExcelSalario(inputStream);
        List<Salario> salvos = repository.saveAll(lista);
        return salvos.stream()
                .map(SalarioResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<SalarioRequest> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::mapearParaDTO)
                .toList();
    }

    public SalarioRequest buscarPorId(Long id) {
        Salario salario = buscarSalario(id);
        return mapearParaDTO(salario);
    }

    public SalarioRequest cadastrar(SalarioRequest request) {
        Salario salario = new Salario(request);
        return mapearParaDTO(repository.save(salario));
    }

    public Optional<SalarioRequest> atualizar(Long id, SalarioRequest request) {
        Salario salario = buscarSalario(id);

        salario.setVigencia(request.vigencia());
        salario.setLei(request.lei());
        salario.setValor(request.valor());

        return Optional.of(mapearParaDTO(repository.save(salario)));
    }

    public Optional<SalarioRequest> atualizarAtributo(Long id, SalarioRequest request) {
        Salario salario = buscarSalario(id);
        atualizarDadosSalario(salario, request);

        return Optional.of(mapearParaDTO(repository.save(salario)));
    }

    public void deletar(Long id) {
        Salario salario = buscarSalario(id);
        repository.delete(salario);
    }

    private Salario buscarSalario(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Índice com ID " + id + " não localizado"));
    }

    private SalarioRequest mapearParaDTO(Salario salario) {
        return new SalarioRequest(
                salario.getVigencia(),
                salario.getLei(),
                salario.getValor()
        );
    }

    private void atualizarDadosSalario(Salario salario, SalarioRequest request) {
        if (request.vigencia() != null) salario.setVigencia(request.vigencia());
        if (request.lei() != null) salario.setLei(request.lei());
        if (request.valor() != null) salario.setValor(request.valor());
    }

    public Optional<Salario> buscarPorDataFixacao(LocalDate data) {
        return repository.findFirstByVigenciaLessThanEqualOrderByVigenciaDesc(data);
    }


}

