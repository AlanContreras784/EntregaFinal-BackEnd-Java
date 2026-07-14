package com.techlab.productos_ecologicos.config;

import com.techlab.productos_ecologicos.jwt.JwtFilter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            // CSRF no es necesario con JWT: los tokens ya protegen contra ese tipo de ataque.
            .csrf(csrf -> csrf.disable())
            // CORS: reemplaza WebConfig. Permite requests desde el frontend.
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            .authorizeHttpRequests(auth -> auth
                // login y register son públicos — no requieren token
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                //productos y categorias son publicos - no requieren token
                .requestMatchers(HttpMethod.GET, "/productos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/categorias/**").permitAll()
                //
                .requestMatchers(HttpMethod.POST, "/productos/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/productos/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/productos/**").permitAll()
                //.requestMatchers(HttpMethod.POST, "/productos/**").hasRole("ADMIN")
                // .requestMatchers(HttpMethod.PUT, "/productos/**").hasRole("ADMIN")
                // .requestMatchers(HttpMethod.DELETE, "/productos/**").hasRole("ADMIN")
                // todo lo demás requiere un token válido
                .anyRequest().authenticated()
            )

            // STATELESS: no hay sesiones HTTP.
            // Cada request se autentica con el token — el servidor no guarda estado.
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authenticationProvider(authProvider)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
)
            // Nuestro JwtFilter se ejecuta ANTES del filtro de autenticación de Spring.
            // Si no hacemos esto, Spring Security intenta autenticar antes de que el
            // filtro haya tenido la oportunidad de cargar el SecurityContext.
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

            .build();
    }

    // Configuración CORS equivalente a la que tenía WebConfig
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("""
                {
                    "error": "FORBIDDEN",
                    "message": "No tienes permisos para realizar esta acción"
                }
            """);
        };
    }


    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            response.getWriter().write("""
                {
                    "error": "UNAUTHORIZED",
                    "message": "Debes iniciar sesión para acceder a este recurso."
                }
            """);
        };
    }
}
