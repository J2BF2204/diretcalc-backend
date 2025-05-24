package com.projetointegrador.diretcalcbackend.service;

import com.projetointegrador.diretcalcbackend.dto.request.IndiceRequest;
import com.projetointegrador.diretcalcbackend.dto.response.IndiceResponse;
import com.projetointegrador.diretcalcbackend.model.Indice;
import com.projetointegrador.diretcalcbackend.repository.IndiceRepository;
import com.projetointegrador.diretcalcbackend.utils.ExcelParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IndiceService {

    private final IndiceRepository repository;

    public IndiceService(IndiceRepository repository) {
        this.repository = repository;
    }

    public List<IndiceResponse> importarExcel(InputStream inputStream) throws IOException {
        ExcelParser parser = new ExcelParser();
        List<Indice> lista = parser.parseExcelIndice(inputStream);
        List<Indice> salvos = repository.saveAll(lista);
        return salvos.stream()
                .map(IndiceResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<IndiceRequest> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::mapearParaDTO)
                .toList();
    }

    public IndiceRequest buscarPorId(Long id) {
        Indice indice = buscarIndice(id);
        return mapearParaDTO(indice);
    }

    public IndiceRequest cadastrar(IndiceRequest request) {
        Indice indice = new Indice(request);
        return mapearParaDTO(repository.save(indice));
    }

    public Optional<IndiceRequest> atualizar(Long id, IndiceRequest request) {
        Indice indice = buscarIndice(id);

        indice.setId(id);
        indice.setVigencia(request.vigencia());
        indice.setIndiceMonetario(request.indiceMonetario());
        indice.setIndiceIpcae(request.indiceIpcae());
        indice.setSelicMensal(request.selicMensal());
        indice.setSelicAcumulada(request.selicAcumulada());

        return Optional.of(mapearParaDTO(repository.save(indice)));
    }

    public Optional<IndiceRequest> atualizarAtributo(Long id, IndiceRequest request) {
        Indice indice = buscarIndice(id);
        atualizarDadosIndice(indice, request);

        return Optional.of(mapearParaDTO(repository.save(indice)));
    }

    public void deletar(Long id) {
        Indice indice = buscarIndice(id);
        repository.delete(indice);
    }

    private Indice buscarIndice(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Índice com ID " + id + " não localizado"));
    }

    private IndiceRequest mapearParaDTO(Indice indice) {
        return new IndiceRequest(
                indice.getVigencia(),
                indice.getIndiceMonetario(),
                indice.getIndiceIpcae(),
                indice.getSelicMensal(),
                indice.getSelicAcumulada()
        );
    }

    private void atualizarDadosIndice(Indice indice, IndiceRequest request) {
        if (request.vigencia() != null) indice.setVigencia(request.vigencia());
        if (request.indiceMonetario() != null) indice.setIndiceMonetario(request.indiceMonetario());
        if (request.indiceIpcae() != null) indice.setIndiceIpcae(request.indiceIpcae());
        if (request.selicMensal() != null) indice.setSelicMensal(request.selicMensal());
        if (request.selicAcumulada() != null) indice.setSelicAcumulada(request.selicAcumulada());

    }


    public BigDecimal buscarSelicAnterior(LocalDate data) {
        YearMonth mesAnterior = YearMonth.from(data).minusMonths(1);
        LocalDate inicio = mesAnterior.atDay(1);
        LocalDate fim = mesAnterior.atEndOfMonth();

        return repository.findByVigenciaBetween(inicio, fim)
                .map(Indice::getSelicAcumulada)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal buscarIndiceMonetario(LocalDate data) {
        YearMonth mes = YearMonth.from(data);
        LocalDate inicio = mes.atDay(1);
        LocalDate fim = mes.atEndOfMonth();

        return repository.findByVigenciaBetween(inicio, fim)
                .map(Indice::getIndiceMonetario)
                .orElse(BigDecimal.ONE); // Retorna 1 se não encontrar, para não afetar multiplicações
    }


}

