package br.com.divMaster.dto;


import lombok.Data;

/**
 * DTO para requisições de autenticação e registro.
 * Lombok gera getters, setters e construtor padrão.
 */
@Data
public class AuthRequestDTO {
    private String username;
    private String password;

    // Getters and Setters
}
