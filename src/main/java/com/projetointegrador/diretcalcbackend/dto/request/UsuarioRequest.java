package com.projetointegrador.diretcalcbackend.dto.request;

import com.projetointegrador.diretcalcbackend.enums.Role;
import jakarta.validation.constraints.Email;

import java.util.Set;

public record UsuarioRequest(
        Long id,
        String nome,
        String email,
        String senha,
        Set<Role> roles
) {
}
