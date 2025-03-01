package com.example.Examen2.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String generarToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(secretKey)
                .compact();
    }

    public String obtenerUsername(String token) {
        Claims claims = obtenerClaims(token);
        if (claims == null) {
            throw new RuntimeException("Token inválido o expirado");
        }
        return claims.getSubject();
    }

    private Claims obtenerClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // Manejo de token expirado
            System.out.println("El token ha expirado: " + e.getMessage());
            return null; // O lanzar una excepción personalizada
        } catch (io.jsonwebtoken.SignatureException e) {
            // Manejo de firma no válida
            System.out.println("Firma de token no válida: " + e.getMessage());
            return null; // O lanzar una excepción personalizada
        } catch (Exception e) {
            // Manejo de otros errores
            System.out.println("Error al obtener claims: " + e.getMessage());
            return null; // O lanzar una excepción personalizada
        }
    }

    public boolean esTokenValido(String token, String username) {
        return (username.equals(obtenerUsername(token)) && !esTokenExpirado(token));
    }



    private boolean esTokenExpirado(String token) {
        return obtenerClaims(token).getExpiration().before(new Date());
    }
}