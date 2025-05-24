package com.projetointegrador.diretcalcbackend.controller;

import com.projetointegrador.diretcalcbackend.dto.request.HonorarioArt85Request;
import com.projetointegrador.diretcalcbackend.dto.response.HonorarioArt85Response;
import com.projetointegrador.diretcalcbackend.service.HonorarioArt85Service;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/honorarios/art85")
@CrossOrigin(origins = "*")
public class HonorarioArt85Controller {

    private final HonorarioArt85Service service;

    public HonorarioArt85Controller(HonorarioArt85Service service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<HonorarioArt85Response>> listarTodos() {
        return ResponseEntity.status(HttpStatus.OK).body(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HonorarioArt85Response> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarPorId(id));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<HonorarioArt85Response> cadastrar(@RequestBody @Valid HonorarioArt85Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HonorarioArt85Response> atualizar(@PathVariable Long id, @RequestBody @Valid HonorarioArt85Request request) {
        return service.atualizar(id, request)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HonorarioArt85Response> atualizarAtributo(@PathVariable Long id, @RequestBody HonorarioArt85Request request) {
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

