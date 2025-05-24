package com.projetointegrador.diretcalcbackend.controller;

import com.projetointegrador.diretcalcbackend.dto.request.SalarioRequest;
import com.projetointegrador.diretcalcbackend.dto.response.SalarioResponse;
import com.projetointegrador.diretcalcbackend.service.SalarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/salarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SalarioController {

    private final SalarioService service;

    public SalarioController(SalarioService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<List<SalarioResponse>> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<SalarioResponse> lista = service.importarExcel(file.getInputStream());
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            e.printStackTrace(); // Isso mostra o erro real no console
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<SalarioRequest>> listarTodos() {
        return ResponseEntity.status(HttpStatus.OK).body(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalarioRequest> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarPorId(id));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<SalarioRequest> cadastrar(@RequestBody @Valid SalarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalarioRequest> atualizar(@PathVariable Long id, @RequestBody @Valid SalarioRequest request) {
        return service.atualizar(id, request)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SalarioRequest> atualizarAtributo(@PathVariable Long id, @RequestBody SalarioRequest request) {
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

