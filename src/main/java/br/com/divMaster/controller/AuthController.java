package br.com.divMaster.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.divMaster.dto.AuthRequestDTO;
import br.com.divMaster.dto.AuthResponseDTO;
import br.com.divMaster.entity.User;
import br.com.divMaster.security.JwtUtil;
import br.com.divMaster.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtil.generateAccessToken(authRequest.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(authRequest.getUsername());

        return new AuthResponseDTO(accessToken, refreshToken);
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/update")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/refresh-token")
    public AuthResponseDTO refreshAccessToken(@RequestBody AuthResponseDTO refreshTokenDTO) {
        String newAccessToken = jwtUtil.generateAccessToken(jwtUtil.extractUsername(refreshTokenDTO.getRefreshToken()));
        return new AuthResponseDTO(newAccessToken, refreshTokenDTO.getRefreshToken());
    }
}
