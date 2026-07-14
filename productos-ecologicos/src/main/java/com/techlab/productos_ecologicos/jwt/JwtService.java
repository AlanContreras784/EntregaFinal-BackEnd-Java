package com.techlab.productos_ecologicos.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Clave secreta en Base64 para firmar el token.
    // En producción va en application.properties y nunca se sube al repositorio.
    private static final String SECRET_KEY =
        "586E3272357538782F413F4428472B4B6250655368566B597033733676397924";

    // Genera un token con los datos básicos del usuario
    public String generarToken(UserDetails usuario) {
        return generarToken(new HashMap<>(), usuario);
    }

    // Genera un token con claims adicionales (datos extra a incluir en el payload)
    public String generarToken(Map<String, Object> claimsExtra, UserDetails usuario) {
        return Jwts.builder()
                .claims(claimsExtra)
                // subject: identificador principal — el username queda en el payload como "sub"
                .subject(usuario.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                // Expiración: 24 horas
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getKey())
                .compact(); // genera el string eyJ...
    }

    // Construye la clave criptográfica a partir del SECRET_KEY
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extrae el username del payload del token
    public String obtenerUsername(String token) {
        return obtenerClaim(token, Claims::getSubject);
    }

    // Valida que el token sea correcto y corresponda al usuario dado.
    // Es válido si: el username coincide Y el token no expiró.
    public boolean esTokenValido(String token, UserDetails usuario) {
        final String username = obtenerUsername(token);
        return username.equals(usuario.getUsername()) && !estaExpirado(token);
    }

    // Obtiene todos los claims del token verificando la firma contra la clave secreta.
    // Si la firma no coincide, lanza una excepción.
    private Claims obtenerTodosLosClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Obtiene un claim específico usando una función como parámetro.
    // Permite extraer cualquier campo del token con el mismo método:
    //   obtenerClaim(token, Claims::getSubject)    → devuelve el username
    //   obtenerClaim(token, Claims::getExpiration) → devuelve la fecha de expiración
    public <T> T obtenerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = obtenerTodosLosClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date obtenerExpiracion(String token) {
        return obtenerClaim(token, Claims::getExpiration);
    }

    // Devuelve true si la fecha de expiración del token es anterior a la fecha actual
    private boolean estaExpirado(String token) {
        return obtenerExpiracion(token).before(new Date());
    }
}
