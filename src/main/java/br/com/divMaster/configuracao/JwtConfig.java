package br.com.divMaster.configuracao;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String secretKey;
    private Long accessTokenExpiration;
    private Long refreshTokenExpiration;

    // Getters e Setters
}
