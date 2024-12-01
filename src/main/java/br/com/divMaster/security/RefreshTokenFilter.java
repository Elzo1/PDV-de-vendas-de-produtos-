package br.com.divMaster.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RefreshTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public RefreshTokenFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String refreshToken = request.getHeader("Refresh-Token");

        if (refreshToken != null) {
            // Obter o 'UserDetails' da autenticação atual
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                // Validar o token passando tanto o token quanto o 'UserDetails'
                if (jwtUtil.validateToken(refreshToken, userDetails)) {
                    String username = jwtUtil.extractUsername(refreshToken);
                    String newAccessToken = jwtUtil.generateAccessToken(username);
                    response.setHeader("New-Access-Token", newAccessToken);
                }
            }
        }

        chain.doFilter(request, response);
    }
}
