package com.example.Examen2.security;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String username;

    public JwtAuthenticationToken(String username) {
        super(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))); // Asumimos un rol "USER" por defecto
        this.username = username;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;  // JWT ya contiene toda la información de autenticación
    }

    @Override
    public Object getPrincipal() {
        return username;  // El "principal" es el nombre de usuario
    }
}