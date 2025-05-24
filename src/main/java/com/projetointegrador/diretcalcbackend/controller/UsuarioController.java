package com.projetointegrador.diretcalcbackend.controller;

import com.projetointegrador.diretcalcbackend.dto.response.UsuarioResponse;
import com.projetointegrador.diretcalcbackend.dto.request.UsuarioLoginRequest;
import com.projetointegrador.diretcalcbackend.dto.request.UsuarioRequest;
import com.projetointegrador.diretcalcbackend.model.Usuario;
import com.projetointegrador.diretcalcbackend.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.status(HttpStatus.OK).body(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarPorId(id));
    }

    @GetMapping("/buscar/{email}")
    public ResponseEntity<UsuarioResponse> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarPorEmail(email));
    }

    @PostMapping("/logar")
    public ResponseEntity<UsuarioResponse> autenticarUsuario(@RequestBody UsuarioLoginRequest request) {
        return service.autenticarUsuario(request)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponse> cadastrarUsuario(@RequestBody @Valid UsuarioRequest request, Authentication authentication) {
        UsuarioResponse response = service.cadastrar(request, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<UsuarioResponse> atualizar(@RequestBody @Valid UsuarioRequest request) {
        return service.atualizar(request)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/atualizar")
    public ResponseEntity<UsuarioResponse> atualizarAtributo(@RequestBody UsuarioRequest request) {
        return service.atualizarAtributo(request)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername(); // pega o email do usuário logado
        service.deletar(id, email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // HTTP 204
    }

}