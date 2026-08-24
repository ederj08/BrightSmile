package com.brightsmile.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    // Clave secreta para firmar los tokens
    // En producción esto iría en variables de entorno
    private final String SECRET = "brightsmile-dental-secret-key-2026-muy-segura";

    // Tiempo de expiración: 24 horas en milisegundos
    private final long EXPIRATION = 86400000;

    // Genera la clave de firma a partir del string SECRET
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Genera un token JWT para un usuario con sus roles
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .subject(username)           // quien es el usuario
                .claim("roles", roles)        // sus roles dentro del token
                .issuedAt(new Date())         // fecha de creación
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION)) // fecha de expiración
                .signWith(getKey())           // firma con la clave secreta
                .compact();                   // construye el token como String
    }

    // Extrae todos los datos (claims) del token
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())         // verifica que la firma sea válida
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Extrae solo el username del token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Extrae los roles del token
    public List<String> extractRoles(String token) {
        return extractClaims(token).get("roles", List.class);
    }

    // Verifica si el token es válido y no ha expirado
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}