package com.example.Examen2.controller;

import com.example.Examen2.model.JwtRequest;
import com.example.Examen2.model.JwtResponse;
import com.example.Examen2.model.User;
import com.example.Examen2.security.JwtUtil;
import com.example.Examen2.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder; // Para codificar la contraseña

    // Inyección de dependencias a través del constructor
    public AuthController(JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Endpoint de login (ya existente)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtil.generarToken(jwtRequest.getUsername());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    // Endpoint de recurso protegido (ya existente)
    @GetMapping("/home")
    public ResponseEntity<String> getProtectedResource() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return ResponseEntity.ok("Acceso permitido a recurso protegido para el usuario: " + username);
        } else {
            return ResponseEntity.status(403).body("Acceso denegado");
        }
    }

    // Endpoint para registrar un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Verificar si el nombre de usuario ya está en uso
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso.");
        }

        // Codificar la contraseña antes de guardar
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Guardar el nuevo usuario
        User savedUser = userService.saveUser(user);

        return ResponseEntity.ok("Usuario registrado con éxito: " + savedUser.getUsername());
    }
}
