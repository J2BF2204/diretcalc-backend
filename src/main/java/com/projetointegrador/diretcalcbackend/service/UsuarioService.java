package com.projetointegrador.diretcalcbackend.service;

import com.projetointegrador.diretcalcbackend.dto.request.UsuarioLoginRequest;
import com.projetointegrador.diretcalcbackend.dto.request.UsuarioRequest;
import com.projetointegrador.diretcalcbackend.dto.response.UsuarioResponse;
import com.projetointegrador.diretcalcbackend.enums.Role;
import com.projetointegrador.diretcalcbackend.mapper.UsuarioMapper;
import com.projetointegrador.diretcalcbackend.model.Usuario;
import com.projetointegrador.diretcalcbackend.repository.UsuarioRepository;
import com.projetointegrador.diretcalcbackend.security.JwtService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioMapper usuarioMapper; // injetando o mapper

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.name}")
    private String adminName;

    @PostConstruct
    public void criarAdminPadrao() {
        if (usuarioRepository.findByEmail(adminEmail).isEmpty()) {
            Usuario admin = Usuario.builder()
                    .nome(adminName)
                    .email(adminEmail)
                    .senha(passwordEncoder.encode(adminPassword))
                    .roles(Set.of(Role.ADMIN))
                    .build();
            usuarioRepository.save(admin);
            System.out.println("✅ Usuário ADMIN criado com sucesso!");
        }
    }

    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toResponseSemToken)
                .toList();
    }

    public UsuarioResponse buscarPorId(Long id) {
        return usuarioMapper.toResponseSemToken(buscarUsuario(id));
    }

    public UsuarioResponse buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(usuarioMapper::toResponseSemToken)
                .orElseThrow(() -> new IllegalArgumentException("Usuário com e-mail " + email + " não localizado"));
    }


    public UsuarioResponse cadastrar(UsuarioRequest request, Authentication authentication) {
        verificarSeEmailJaExiste(request.email());

        Usuario usuario = usuarioMapper.toEntity(request);

        if (usuarioDesejaSerAdmin(usuario)) {
            if (authentication == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "É necessário estar autenticado como ADMIN para criar outro ADMIN");
            }
            validarCriacaoDeAdmin(authentication);
        }

        atribuirRolePadraoSeNecessario(usuario);
        usuario.setSenha(criptografarSenha(request.senha()));

        Usuario salvo = usuarioRepository.save(usuario);

        return usuarioMapper.toResponseSemToken(salvo);
    }

    public Optional<UsuarioResponse> autenticarUsuario(UsuarioLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha()));

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        String token = "Bearer " + jwtService.generateToken(usuario.getEmail(), usuario.getRoles());

        return Optional.of(usuarioMapper.toResponse(usuario, token));
    }

    public Optional<UsuarioResponse> atualizar(UsuarioRequest request) {
        return usuarioRepository.findById(request.id()).map(existingUser -> {
            usuarioRepository.findByEmail(request.email())
                    .filter(user -> !user.getId().equals(request.id()))
                    .ifPresent(user -> {
                        throw new IllegalArgumentException("E-mail cadastrado para outro usuário");
                    });

            existingUser.setNome(request.nome());
            existingUser.setEmail(request.email());

            if (request.senha() != null && !request.senha().isBlank()) {
                existingUser.setSenha(passwordEncoder.encode(request.senha()));
            }

            if (request.roles() != null && !request.roles().isEmpty()) {
                existingUser.setRoles(request.roles());
            }

            return usuarioMapper.toResponseSemToken(usuarioRepository.save(existingUser));
        });
    }

    public Optional<UsuarioResponse> atualizarAtributo(UsuarioRequest request) {
        return atualizar(request);
    }

    public void deletar(Long id, String email) {
        Usuario usuarioAutenticado = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário autenticado não encontrado"));

        Usuario usuarioParaExcluir = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID: " + id));

        if (usuarioParaExcluir.getEmail().equals(adminEmail)) {
            throw new IllegalArgumentException("O usuário ADMIN padrão não pode ser excluído.");
        }

        boolean isAdmin = usuarioAutenticado.getRoles().contains(Role.ADMIN);
        boolean isProprioUsuario = usuarioAutenticado.getId().equals(usuarioParaExcluir.getId());

        if (isAdmin || isProprioUsuario) {
            usuarioRepository.delete(usuarioParaExcluir);
        } else {
            throw new IllegalArgumentException("Você não tem permissão para excluir este usuário.");
        }
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário com ID " + id + " não localizado!"));
    }

    private void verificarSeEmailJaExiste(String email) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("E-mail cadastrado para outro usuário");
        }
    }

    private boolean usuarioDesejaSerAdmin(Usuario usuario) {
        return usuario.getRoles().contains(Role.ADMIN);
    }

    private void validarCriacaoDeAdmin(Authentication authentication) {
        Usuario usuarioLogado = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário autenticado não encontrado"));

        if (!usuarioLogado.getRoles().contains(Role.ADMIN)) {
            throw new IllegalArgumentException("Apenas ADMIN pode criar outro ADMIN!");
        }
    }

    private void atribuirRolePadraoSeNecessario(Usuario usuario) {
        if (usuario.getRoles().isEmpty()) {
            usuario.getRoles().add(Role.USER);
        }
    }

    private String criptografarSenha(String senha) {
        return passwordEncoder.encode(senha);
    }
}
