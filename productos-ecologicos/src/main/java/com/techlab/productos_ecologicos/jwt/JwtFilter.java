package com.techlab.productos_ecologicos.jwt;

import com.techlab.productos_ecologicos.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Extraer el token del header Authorization
        try{
        final String token = extraerTokenDelRequest(request);

        // 2. Si no hay token, continuar la cadena sin autenticar.
        //    Spring Security bloqueará el request si el endpoint lo requiere.
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        // 3. Extraer el username del payload del token
        final String username = jwtService.obtenerUsername(token);

        // 4. Condición doble:
        //    - username != null: el token tenía un username válido
        //    - getAuthentication() == null: el usuario no fue autenticado antes en esta cadena
        //    La segunda condición evita pisar una autenticación ya establecida.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 5. Buscar el usuario en la base de datos
            UserDetails usuario = usuarioRepository.findByUsername(username)
                    .orElseThrow();

            // 6. Validar: ¿el token corresponde a este usuario y no expiró?
            if (usuario!=null && jwtService.esTokenValido(token, usuario)) {

                // 7. Crear el objeto de autenticación.
                //    Parámetros: (usuario, credenciales, permisos)
                //    Las credenciales van null — la contraseña ya fue verificada en el login,
                //    no tiene sentido cargarla en memoria en este punto.
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                usuario, null, usuario.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                // Registrar en el SecurityContextHolder.
                // A partir de acá Spring Security sabe que este usuario está autenticado.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 8. Continuar con el siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    
}catch(Exception e){
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");

    response.getWriter().write("""
    {
        "error": "UNAUTHORIZED",
        "message": "Token inválido o alterado"
    }
    """);
}
            }
    // Extrae el token del header "Authorization: Bearer <token>"
    // Corta los primeros 7 caracteres ("Bearer ") para quedarse solo con el token
    private String extraerTokenDelRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
