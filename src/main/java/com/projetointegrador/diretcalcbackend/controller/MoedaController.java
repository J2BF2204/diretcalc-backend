package com.projetointegrador.diretcalcbackend.controller;

import com.projetointegrador.diretcalcbackend.dto.request.MoedaRequest;
import com.projetointegrador.diretcalcbackend.dto.response.MoedaResponse;
import com.projetointegrador.diretcalcbackend.service.MoedaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/moedas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MoedaController {

    private final MoedaService service;

    public MoedaController(MoedaService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<List<MoedaResponse>> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<MoedaResponse> lista = service.importarExcel(file.getInputStream());
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            e.printStackTrace(); // Isso mostra o erro real no console
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<MoedaRequest>> listarTodos() {
        return ResponseEntity.status(HttpStatus.OK).body(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoedaRequest> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarPorId(id));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<MoedaRequest> cadastrar(@RequestBody @Valid MoedaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MoedaRequest> atualizar(@PathVariable Long id, @RequestBody @Valid MoedaRequest request) {
        return service.atualizar(id, request)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MoedaRequest> atualizarAtributo(@PathVariable Long id, @RequestBody MoedaRequest request) {
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

