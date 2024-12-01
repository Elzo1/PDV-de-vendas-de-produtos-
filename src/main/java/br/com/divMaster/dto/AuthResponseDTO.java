package br.com.divMaster.dto;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respostas de autenticação.
 * Lombok gera getters, setters e construtor com todos os parâmetros.
 */

@Data
@NoArgsConstructor
public class AuthResponseDTO {
    private String accessToken;
    private String refreshToken;

    public AuthResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
