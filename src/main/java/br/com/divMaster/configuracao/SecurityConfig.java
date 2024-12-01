package br.com.divMaster.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.divMaster.security.JwtAuthenticationFilter;
import br.com.divMaster.security.JwtUtil;
import br.com.divMaster.security.RefreshTokenFilter;
import br.com.divMaster.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtUtil jwtUtil;
	private final UserService userService;

	public SecurityConfig(JwtUtil jwtUtil, UserService userService) {
		this.jwtUtil = jwtUtil;
		this.userService = userService;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/refresh-token")
								.permitAll().anyRequest().authenticated())
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userService),
						UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(new RefreshTokenFilter(jwtUtil), JwtAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.authenticationProvider(authenticationProvider());
		return authenticationManagerBuilder.build();
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userService);
		return provider;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
