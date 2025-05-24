package com.projetointegrador.diretcalcbackend.service;

import com.projetointegrador.diretcalcbackend.dto.request.HonorarioArt85Request;
import com.projetointegrador.diretcalcbackend.dto.request.HonorarioSimplesRequest;
import com.projetointegrador.diretcalcbackend.dto.response.CalculoResponse;
import com.projetointegrador.diretcalcbackend.enums.Aliquota;
import com.projetointegrador.diretcalcbackend.enums.Atualizacao;
import com.projetointegrador.diretcalcbackend.model.Indice;
import com.projetointegrador.diretcalcbackend.model.Salario;
import com.projetointegrador.diretcalcbackend.repository.IndiceRepository;
import com.projetointegrador.diretcalcbackend.repository.SalarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

@Service
public class CalculoService {

    private final IndiceService indiceService;
    private final SalarioService salarioService;
    private final SalarioRepository repository;

    public CalculoService(IndiceService indiceService, SalarioRepository repository, SalarioService salarioService) {
        this.indiceService = indiceService;
        this.repository = repository;
        this.salarioService = salarioService;
    }

    public CalculoResponse calcularHonorarioSimples(HonorarioSimplesRequest request) {
        LocalDate dataSelic = LocalDate.of(2021, 12, 1);
        BigDecimal fatorAtualizacao = BigDecimal.ONE;

        if (request.atualizacao() == Atualizacao.SELIC) {
            LocalDate fixacaoVerba = request.fixacaoVerba();
            LocalDate dataFinal = request.dataFinal();

            boolean usaSelic = (fixacaoVerba != null && !fixacaoVerba.isBefore(dataSelic))
                    || !dataFinal.isBefore(dataSelic);

            if (usaSelic) {
                BigDecimal acumuladoFinal = indiceService.buscarSelicAnterior(dataFinal);
                BigDecimal acumuladoInicial = (request.dataInicial().isAfter(dataSelic))
                        ? indiceService.buscarSelicAnterior(request.dataInicial())
                        : indiceService.buscarSelicAnterior(dataSelic);

                fatorAtualizacao = acumuladoFinal
                        .subtract(acumuladoInicial)
                        .divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP)
                        .add(BigDecimal.ONE);
            }
        }

        BigDecimal indiceFinal = indiceService.buscarIndiceMonetario(request.dataFinal());
        BigDecimal indiceInicial = indiceService.buscarIndiceMonetario(request.dataInicial());

        BigDecimal percentualDecimal = request.percentual()
                .divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP);

        BigDecimal valorCorrigido = indiceFinal
                .divide(indiceInicial, 8, RoundingMode.HALF_UP)
                .multiply(request.valorBase())
                .multiply(fatorAtualizacao);

        BigDecimal valorHonorario = valorCorrigido.multiply(percentualDecimal);

        BigDecimal totalCorrigidoComHonorarios = valorCorrigido.add(valorHonorario);

        BigDecimal totalComDeducoes = totalCorrigidoComHonorarios.subtract(request.pagamentoParcial());

        return new CalculoResponse(fatorAtualizacao, valorCorrigido, valorHonorario, totalCorrigidoComHonorarios, totalComDeducoes);
    }


//    public CalculoResponse calcularHonorarioArt85(HonorarioArt85Request request) {
//        LocalDate dataSelic = LocalDate.of(2021, 12, 1);
//        BigDecimal fatorAtualizacao = BigDecimal.ONE;
//
//        // Buscar o salário mínimo vigente na data da fixação da verba
//        BigDecimal salarioMinimo = salarioService
//                .buscarPorDataFixacao(request.fixacaoVerba())
//                .orElseThrow(() -> new RuntimeException("Salário mínimo não encontrado"))
//                .getValor();
//
//
//        // Aqui o usuário informa o valor base direto:
//        BigDecimal valorBase = request.valorBase();
//
//        // Calcula o múltiplo de salários mínimos a partir do valor base informado
//        BigDecimal multiploSalario = valorBase.divide(salarioMinimo, 8, RoundingMode.HALF_UP);
//
//        // Calcula a alíquota conforme múltiplo e mínima ou máxima
//        BigDecimal aliquotaDecimal = calcularAliquota(multiploSalario, request.aliquota());
//
//        // Cálculo de fator SELIC (mesma lógica)
//        if (request.atualizacao() == Atualizacao.SELIC) {
//            LocalDate fixacaoVerba = request.fixacaoVerba();
//            LocalDate dataFinal = request.dataFinal();
//
//            boolean usaSelic = (fixacaoVerba != null && !fixacaoVerba.isBefore(dataSelic))
//                    || !dataFinal.isBefore(dataSelic);
//
//            if (usaSelic) {
//                BigDecimal acumuladoFinal = indiceService.buscarSelicAnterior(dataFinal);
//                BigDecimal acumuladoInicial = (request.dataInicial().isAfter(dataSelic))
//                        ? indiceService.buscarSelicAnterior(request.dataInicial())
//                        : indiceService.buscarSelicAnterior(dataSelic);
//
//                fatorAtualizacao = acumuladoFinal
//                        .subtract(acumuladoInicial)
//                        .divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP)
//                        .add(BigDecimal.ONE);
//            }
//        }
//
//        BigDecimal indiceFinal = indiceService.buscarIndiceMonetario(request.dataFinal());
//        BigDecimal indiceInicial = indiceService.buscarIndiceMonetario(request.dataInicial());
//        BigDecimal indiceFixacao = indiceService.buscarIndiceMonetario(request.fixacaoVerba());
//
//        BigDecimal percentualDecimal = aliquotaDecimal.divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP);
//
//
//        BigDecimal valorCorrigido = indiceFixacao
//                .divide(indiceInicial, 8, RoundingMode.HALF_UP)
//                .multiply(valorBase)
//                .multiply(fatorAtualizacao);
//
//
//        BigDecimal valorHonorario = valorCorrigido.multiply(percentualDecimal);
//
//        BigDecimal totalCorrigidoComHonorarios = valorCorrigido.add(valorHonorario);
//
//        BigDecimal totalComDeducoes = totalCorrigidoComHonorarios.subtract(request.pagamentoParcial());
//
//        return new CalculoResponse(fatorAtualizacao, valorCorrigido, valorHonorario, totalCorrigidoComHonorarios, totalComDeducoes);
//    }

    // Parte do CalculoService
    public CalculoResponse calcularHonorarioArt85(HonorarioArt85Request request) {
        BigDecimal salarioMinimo = salarioService.buscarPorDataFixacao(request.fixacaoVerba())
                .orElseThrow(() -> new RuntimeException("Salário mínimo não encontrado para a data informada."))
                .getValor();

        BigDecimal fatorAtualizacao = BigDecimal.ONE;

        BigDecimal valorCorrigido = indiceService.buscarIndiceMonetario(request.dataInicial())
                .divide(indiceService.buscarIndiceMonetario(request.fixacaoVerba()), 10, RoundingMode.HALF_UP)
                .multiply(request.valorBase())
                .multiply(fatorAtualizacao);

        Map<BigDecimal, BigDecimal> percentuaisPorFaixa = switch (request.aliquota()) {
            case MINIMA -> Map.of(
                    BigDecimal.valueOf(200), new BigDecimal("0.10"),
                    BigDecimal.valueOf(2000), new BigDecimal("0.08"),
                    BigDecimal.valueOf(20000), new BigDecimal("0.05"),
                    BigDecimal.valueOf(100000), new BigDecimal("0.03"),
                    BigDecimal.valueOf(Long.MAX_VALUE), new BigDecimal("0.01")
            );
            case MAXIMA -> Map.of(
                    BigDecimal.valueOf(200), new BigDecimal("0.20"),
                    BigDecimal.valueOf(2000), new BigDecimal("0.10"),
                    BigDecimal.valueOf(20000), new BigDecimal("0.08"),
                    BigDecimal.valueOf(100000), new BigDecimal("0.05"),
                    BigDecimal.valueOf(Long.MAX_VALUE), new BigDecimal("0.03")
            );
        };

        BigDecimal valorHonorario = calcularHonorariosProgressivos(valorCorrigido, salarioMinimo, percentuaisPorFaixa);
        BigDecimal totalCorrigidoComHonorarios = valorCorrigido.add(valorHonorario);
        BigDecimal totalComDeducoes = totalCorrigidoComHonorarios.subtract(request.pagamentoParcial());

        return new CalculoResponse(
                fatorAtualizacao,
                valorCorrigido,
                valorHonorario,
                totalCorrigidoComHonorarios,
                totalComDeducoes
        );
    }


    private BigDecimal calcularHonorariosProgressivos(BigDecimal valorCorrigido, BigDecimal salarioMinimo, Map<BigDecimal, BigDecimal> percentuaisPorFaixa) {
        BigDecimal totalHonorarios = BigDecimal.ZERO;
        BigDecimal[] limites = {
                salarioMinimo.multiply(BigDecimal.valueOf(200)),
                salarioMinimo.multiply(BigDecimal.valueOf(2000)),
                salarioMinimo.multiply(BigDecimal.valueOf(20000)),
                salarioMinimo.multiply(BigDecimal.valueOf(100000))
        };

        BigDecimal[] percentuais = {
                percentuaisPorFaixa.getOrDefault(BigDecimal.valueOf(200), BigDecimal.ZERO),
                percentuaisPorFaixa.getOrDefault(BigDecimal.valueOf(2000), BigDecimal.ZERO),
                percentuaisPorFaixa.getOrDefault(BigDecimal.valueOf(20000), BigDecimal.ZERO),
                percentuaisPorFaixa.getOrDefault(BigDecimal.valueOf(100000), BigDecimal.ZERO),
                percentuaisPorFaixa.getOrDefault(BigDecimal.valueOf(Long.MAX_VALUE), BigDecimal.ZERO)
        };

        BigDecimal restante = valorCorrigido;
        BigDecimal faixaInicial = BigDecimal.ZERO;

        for (int i = 0; i <= limites.length; i++) {
            BigDecimal limiteAtual = (i < limites.length) ? limites[i] : restante;
            BigDecimal faixa = limiteAtual.subtract(faixaInicial);
            BigDecimal valorNaFaixa = restante.min(faixa);

            totalHonorarios = totalHonorarios.add(valorNaFaixa.multiply(percentuais[i]));

            restante = restante.subtract(valorNaFaixa);
            faixaInicial = limiteAtual;

            if (restante.compareTo(BigDecimal.ZERO) <= 0) break;
        }

        return totalHonorarios.setScale(2, RoundingMode.HALF_UP);
    }


    private BigDecimal calcularAliquota(BigDecimal multiploSalario, Aliquota aliquota) {
        double multiplo = multiploSalario.doubleValue();

        if (aliquota == Aliquota.MAXIMA) {
            if (multiplo <= 200) return BigDecimal.valueOf(20);
            else if (multiplo <= 2000) return BigDecimal.valueOf(10);
            else if (multiplo <= 20000) return BigDecimal.valueOf(8);
            else if (multiplo <= 100000) return BigDecimal.valueOf(5);
            else return BigDecimal.valueOf(3);
        } else { // Aliquota.MINIMA
            if (multiplo <= 200) return BigDecimal.valueOf(10);
            else if (multiplo <= 2000) return BigDecimal.valueOf(8);
            else if (multiplo <= 20000) return BigDecimal.valueOf(5);
            else if (multiplo <= 100000) return BigDecimal.valueOf(3);
            else return BigDecimal.valueOf(1);
        }
    }
}
