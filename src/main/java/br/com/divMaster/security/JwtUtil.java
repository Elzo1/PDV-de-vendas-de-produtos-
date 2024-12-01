package br.com.divMaster.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {

	private static final String SECRET_KEY = "$@%5p7(-9g%&cz$@f4b)uu1^utbcc4_*ls!cj-&)m0m57r)#5o8d3$@l6k*m1m9p#1b3&!";
	private static final Key SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    // Gera o accessToken com tempo de expiração de 1 hora
    public String generateAccessToken(String username) {
        ZonedDateTime zonedNow = LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).atZone(ZoneId.of("America/Sao_Paulo"));

        String token = Jwts.builder()
                .claim("sub", username)
                .setIssuedAt(Date.from(zonedNow.toInstant())) // Data de criação
                .setExpiration(Date.from(zonedNow.plusHours(1).toInstant())) // Expira em 1 hora
                .signWith(SIGNING_KEY)
                .compact();

        log.info("Access token generated for username: {}", username);
        return token;
    }

    // Gera o refreshToken com tempo de expiração de 7 dias
    public String generateRefreshToken(String username) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));

        String token = Jwts.builder()
                .claim("sub", username)
                .setIssuedAt(Date.from(now.toInstant())) // Data de criação
                .setExpiration(Date.from(now.plusDays(7).toInstant())) // Expira em 7 dias
                .signWith(SIGNING_KEY)
                .compact();

        log.info("Refresh token generated for username: {}", username);
        return token;
    }

    // Validação do token e verificação de expiração
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
            log.info("Token validation status for {}: {}", username, isValid);
            return isValid;
        } catch (JwtException e) {
            log.error("Token validation failed", e);
            return false;
        }
    }

    // Verifica se o token está expirado
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getClaims(token).getExpiration();
            boolean expired = expiration.before(new Date());
            log.info("Token expired: {}", expired);
            return expired;
        } catch (ExpiredJwtException e) {
            log.warn("Token has expired", e);
            return true;
        } catch (JwtException e) {
            log.error("Error checking if token is expired", e);
            return true;
        }
    }

    // Extrai o nome de usuário do token
    public String extractUsername(String token) {
        try {
            String username = getClaims(token).getSubject();
            log.info("Extracted username: {}", username);
            return username;
        } catch (JwtException e) {
            log.error("Failed to extract username from token", e);
            throw e;
        }
    }

    // Extrai as informações (claims) do token com tratamento de exceções
    private Claims getClaims(String token) {
        try {
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build();
            Jws<Claims> jwsClaims = parser.parseClaimsJws(token);
            return jwsClaims.getBody();
        } catch (ExpiredJwtException e) {
            log.warn("Token has expired", e);
            throw e;
        } catch (JwtException e) {
            log.error("Invalid or malformed token", e);
            throw e;
        }
    }

    // Cria um AuthenticationToken baseado no JWT
    public UsernamePasswordAuthenticationToken getAuthenticationToken(String token, UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
    }
}
