package com.projetointegrador.diretcalcbackend.controller;

import com.projetointegrador.diretcalcbackend.dto.request.HonorarioArt85Request;
import com.projetointegrador.diretcalcbackend.dto.request.HonorarioSimplesRequest;
import com.projetointegrador.diretcalcbackend.dto.response.CalculoResponse;
import com.projetointegrador.diretcalcbackend.service.CalculoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calculos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CalculoController {

    private final CalculoService service;

    public CalculoController(CalculoService service) {
        this.service = service;
    }

    @PostMapping("/simples")
    public ResponseEntity<CalculoResponse> calcularHonorariosSimples(@RequestBody HonorarioSimplesRequest request) {
        CalculoResponse resultado = service.calcularHonorarioSimples(request);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/art85")
    public ResponseEntity<CalculoResponse> calcular(@RequestBody HonorarioArt85Request request) {
        CalculoResponse resultado = service.calcularHonorarioArt85(request);
        return ResponseEntity.ok(resultado);
    }

}


