package com.projetointegrador.diretcalcbackend.controller;

import com.projetointegrador.diretcalcbackend.dto.request.HonorarioSimplesRequest;
import com.projetointegrador.diretcalcbackend.dto.response.HonorarioSimplesResponse;
import com.projetointegrador.diretcalcbackend.service.HonorarioSimplesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/honorarios/simples")
@CrossOrigin(origins = "*")
public class HonorarioSimplesController {

    private final HonorarioSimplesService service;

    public HonorarioSimplesController(HonorarioSimplesService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<HonorarioSimplesResponse>> listarTodos() {
        return ResponseEntity.status(HttpStatus.OK).body(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HonorarioSimplesResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarPorId(id));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<HonorarioSimplesResponse> cadastrar(@RequestBody @Valid HonorarioSimplesRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HonorarioSimplesResponse> atualizar(@PathVariable Long id, @RequestBody @Valid HonorarioSimplesRequest request) {
        return service.atualizar(id, request)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HonorarioSimplesResponse> atualizarAtributo(@PathVariable Long id, @RequestBody HonorarioSimplesRequest request) {
        return service.atualizarAtributo(id, request)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

