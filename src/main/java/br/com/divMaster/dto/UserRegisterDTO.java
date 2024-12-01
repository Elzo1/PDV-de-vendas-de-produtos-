package br.com.divMaster.dto;

import java.util.Set;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String username; // Nome de usuário para registro
    private String password; // Senha do usuário
    private Set<String> roles; // As roles são passadas como Strings no DTO
}
