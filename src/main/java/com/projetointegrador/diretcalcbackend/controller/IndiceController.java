package com.projetointegrador.diretcalcbackend.controller;

import com.projetointegrador.diretcalcbackend.dto.request.IndiceRequest;
import com.projetointegrador.diretcalcbackend.dto.response.IndiceResponse;
import com.projetointegrador.diretcalcbackend.service.IndiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/indices")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IndiceController {

    private final IndiceService service;

    public IndiceController(IndiceService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<List<IndiceResponse>> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<IndiceResponse> lista = service.importarExcel(file.getInputStream());
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            e.printStackTrace(); // Isso mostra o erro real no console
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<IndiceRequest>> listarTodos() {
        return ResponseEntity.status(HttpStatus.OK).body(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IndiceRequest> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarPorId(id));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<IndiceRequest> cadastrar(@RequestBody @Valid IndiceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IndiceRequest> atualizar(@PathVariable Long id, @RequestBody @Valid IndiceRequest request) {
        return service.atualizar(id, request)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<IndiceRequest> atualizarAtributo(@PathVariable Long id, @RequestBody IndiceRequest request) {
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

