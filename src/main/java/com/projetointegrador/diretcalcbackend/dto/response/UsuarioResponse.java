package com.projetointegrador.diretcalcbackend.dto.response;

import com.projetointegrador.diretcalcbackend.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        String token,
        Set<Role> roles
) {
}
