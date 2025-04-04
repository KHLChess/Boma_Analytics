package com.bomaanalytics.jwt.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.bomaanalytics.domain.Usuarios;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static Key getSigningKey() {
        return key;
    }
	
    public String extractRFC(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) //Usa la clave generada correctamente
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(UserDetails userDetails) {
    	Map<String, Object> claims = new HashMap<>();

        if (userDetails instanceof Usuarios) { // Verifica si es una instancia de Usuarios
            Usuarios usuario = (Usuarios) userDetails;
            if (usuario.getTipoUsuario() != null) {
                claims.put("role", usuario.getTipoUsuario().name()); // Agregar el rol
                return createToken(claims, usuario.getRfc());
            } else {
                System.out.println("El usuario no tiene un tipo de usuario asignado");
            }
        } else {
            System.out.println("El objeto userDetails no es de tipo Usuarios");
        }

        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(key) //Corrige la firma
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
    	final String username = extractRFC(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
	
}
