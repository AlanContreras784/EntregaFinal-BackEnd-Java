# Implementación de JWT + Spring Security en el proyecto ecommerce

Esta guía agrega autenticación con JWT al proyecto `ecommerce_26138` que ya tiene JPA, MySQL y Lombok. Se construye paso a paso, en el orden en que las dependencias entre clases lo requieren.

---

## Qué se va a agregar

Tres nuevos paquetes dentro de `com.ecommerce.ecommerce`:

```
com.ecommerce.ecommerce
├── auth
│   ├── AuthController.java
│   ├── AuthService.java
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   └── AuthResponse.java
├── config
│   ├── AppConfig.java         ← reemplaza WebConfig.java
│   └── SecurityConfig.java
└── jwt
    ├── JwtService.java
    └── JwtFilter.java
```

Y una nueva entidad en el paquete `model`:

```
model
└── Usuario.java   ← nueva entidad
└── Role.java      ← nuevo enum
```

Al terminar, los endpoints `GET /productos`, `POST /productos`, `PUT /productos/{id}` y `DELETE /productos/{id}` van a requerir un token válido en el header `Authorization`. El endpoint `GET /productos` se puede dejar público si se prefiere — se explica cómo en el Paso 7.

---

## Paso 1 — Agregar las dependencias de Spring Security y JJWT

Abrir `pom.xml` y agregar estas dependencias dentro del bloque `<dependencies>`:

```xml
<!-- Spring Security: intercepta requests y gestiona autenticación y autorización -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- jjwt-api: las interfaces y clases que usamos directamente en el código -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
</dependency>

<!-- jjwt-impl: implementación interna — solo se necesita en tiempo de ejecución -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>

<!-- jjwt-jackson: soporte JSON para el token — solo en tiempo de ejecución -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
```

> **Por qué `runtime` en los dos últimos:** JJWT se divide en API e implementación. Solo usamos las clases de `jjwt-api` en el código. Los otros dos módulos son internos — Spring los carga al arrancar la app, pero no los referenciamos directamente.

Guardar y dejar que Maven descargue las dependencias (el ícono del elefante en IntelliJ, o `mvn install` en terminal).

---

## Paso 2 — Crear la entidad Usuario y el enum Role

Crear los siguientes archivos en el paquete `model`.

### `Role.java`

```java
package com.ecommerce.ecommerce.model;

public enum Role {
    ADMIN,
    USER
}
```

### `Usuario.java`

`Usuario` implementa `UserDetails`, una interfaz de Spring Security. Al implementarla le decimos a Spring Security: *"esta es mi clase de usuario, tratala así"*. Sin esta implementación, Spring Security no sabría cómo trabajar con nuestra entidad.

```java
package com.ecommerce.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"username"})
})
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String nombre;
    private String apellido;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Devuelve los roles del usuario. Spring Security los usa para autorización.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    // Los cuatro métodos siguientes devuelven true para simplificar el proyecto.
    // En producción se implementan con lógica real: cuenta bloqueada tras intentos
    // fallidos, email sin confirmar, credenciales vencidas, etc.
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
```

### `UsuarioRepository.java`

Crear en el paquete `repository`:

```java
package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Spring Security necesita buscar el usuario por username para validar credenciales.
    // JPA genera la query automáticamente a partir del nombre del método.
    Optional<Usuario> findByUsername(String username);
}
```

---

## Paso 3 — Crear JwtService

Crear el paquete `jwt`. Esta clase tiene tres responsabilidades: generar tokens, validarlos y extraer datos de ellos.

La **clave secreta** es lo que hace que los tokens sean auténticos. Si alguien modifica el token sin conocerla, la firma no coincide y el servidor rechaza el request. En producción va en `application.properties` y nunca se sube al repositorio.

```java
package com.ecommerce.ecommerce.jwt;

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
```

---

## Paso 4 — Crear JwtFilter

Este filtro intercepta **cada request** antes de que llegue al controller. El flujo interno tiene 8 pasos — están numerados en los comentarios para seguirlos en orden durante la clase.

`OncePerRequestFilter` garantiza que el filtro se ejecute exactamente una vez por request, sin importar cuántos filtros haya en la cadena.

```java
package com.ecommerce.ecommerce.jwt;

import com.ecommerce.ecommerce.repository.UsuarioRepository;
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
            if (jwtService.esTokenValido(token, usuario)) {

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
```

---

## Paso 5 — Crear AppConfig

Esta clase declara cuatro beans que otros componentes necesitan. Son cuatro piezas que se conectan en cadena:

```
UserDetailsService  →  DaoAuthenticationProvider  →  AuthenticationManager  →  AuthService
PasswordEncoder     ↗
```

- `UserDetailsService` le dice al provider cómo buscar un usuario por username
- `PasswordEncoder` le dice al provider cómo comparar contraseñas con BCrypt
- `DaoAuthenticationProvider` combina los dos anteriores para verificar credenciales
- `AuthenticationManager` delega en el provider y es el que llamamos desde `AuthService.login()`

Crear en el paquete `config`:

```java
package com.ecommerce.ecommerce.config;

import com.ecommerce.ecommerce.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UsuarioRepository usuarioRepository;

    // Cómo cargar un usuario desde la base de datos dado su username.
    // Spring Security llama a esto internamente cuando verifica credenciales.
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No se encontró un usuario con username: " + username));
    }

    // BCrypt para encriptar y comparar contraseñas.
    // Es una función de hash unidireccional: nunca desencripta.
    // Al hacer login, BCrypt vuelve a aplicar el hash a la contraseña ingresada
    // y compara el resultado con el hash guardado en la base de datos.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Combina UserDetailsService y PasswordEncoder.
    // Sabe buscar el usuario Y sabe comparar la contraseña.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // El orquestador de autenticación — delega en el AuthenticationProvider.
    // Es el que llamamos desde AuthService.login() para verificar las credenciales.
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
```

---

## Paso 6 — Eliminar WebConfig y crear SecurityConfig

`WebConfig.java` maneja CORS con `WebMvcConfigurer`. Al agregar Spring Security, la configuración CORS debe moverse a la cadena de filtros de Security. **Eliminar `WebConfig.java`** del paquete `config`.

Crear `SecurityConfig.java` en el mismo paquete:

```java
package com.ecommerce.ecommerce.config;

import com.ecommerce.ecommerce.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
                // todo lo demás requiere un token válido
                .anyRequest().authenticated()
            )

            // STATELESS: no hay sesiones HTTP.
            // Cada request se autentica con el token — el servidor no guarda estado.
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authenticationProvider(authProvider)

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
}
```

> **Nota sobre CORS con `allowedOrigins("*")`:** cuando se usa `*` en los orígenes, no se puede usar `allowCredentials(true)` en el mismo momento. Para el frontend del proyecto no hay problema porque el frontend manda el token en el header `Authorization`, no en cookies.

---

## Paso 7 — Crear el paquete auth

Este paquete contiene los DTOs, la lógica y el controller de autenticación.

### `LoginRequest.java`

```java
package com.ecommerce.ecommerce.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
}
```

### `RegisterRequest.java`

```java
package com.ecommerce.ecommerce.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private String nombre;
    private String apellido;
}
```

### `AuthResponse.java`

```java
package com.ecommerce.ecommerce.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// La respuesta es la misma para login y para registro: siempre devolvemos el token.
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
}
```

### `AuthService.java`

Dos puntos clave antes de leer el código:

**Por qué llamamos `findByUsername` después de autenticar:** `authenticationManager.authenticate(...)` verifica las credenciales pero no devuelve el objeto usuario. Necesitamos el `UserDetails` para que `JwtService` genere el token, así que hacemos una segunda búsqueda.

**BCrypt nunca desencripta:** en el registro, `passwordEncoder.encode(...)` aplica un hash irreversible a la contraseña. Al hacer login, `authenticationManager` aplica BCrypt de nuevo a la contraseña ingresada y compara el resultado. La contraseña original nunca se recupera.

```java
package com.ecommerce.ecommerce.auth;

import com.ecommerce.ecommerce.jwt.JwtService;
import com.ecommerce.ecommerce.model.Role;
import com.ecommerce.ecommerce.model.Usuario;
import com.ecommerce.ecommerce.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        // Esta línea activa toda la cadena interna de Spring Security:
        // busca el usuario por username, aplica BCrypt a la contraseña ingresada,
        // compara con el hash guardado. Si algo falla, lanza una excepción acá.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Si llegamos acá, las credenciales son correctas.
        // Buscamos el usuario para que JwtService pueda generar el token.
        UserDetails usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow();

        String token = jwtService.generarToken(usuario);
        return AuthResponse.builder().token(token).build();
    }

    public AuthResponse register(RegisterRequest request) {
        // Construimos el usuario con la contraseña encriptada con BCrypt.
        // La contraseña original nunca se guarda en la base de datos.
        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .role(Role.USER)
                .build();

        usuarioRepository.save(usuario);

        // El usuario queda logueado inmediatamente al registrarse.
        String token = jwtService.generarToken(usuario);
        return AuthResponse.builder().token(token).build();
    }
}
```

### `AuthController.java`

```java
package com.ecommerce.ecommerce.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
```

---

## Paso 8 — Levantar la aplicación

Arrancar la app. Hibernate crea la tabla `usuario` automáticamente porque `ddl-auto=update` ya estaba configurado en `application.properties`.

Verificar en MySQL que la tabla existe:

```sql
USE ecommerce;
SHOW TABLES;
DESCRIBE usuario;
```

Si la app no arranca, revisar la consola. Los errores más comunes en este punto son:

- **`Consider defining a bean of type 'JwtFilter'`** → revisar que `JwtFilter` tenga `@Component`
- **`Parameter 0 of constructor required a bean of type 'AuthenticationProvider'`** → revisar que `AppConfig` tenga `@Configuration`

---

## Paso 9 — Pruebas con Thunder Client

### Registro

**POST** `http://localhost:8080/auth/register`

Body (JSON):
```json
{
  "username": "miguel",
  "password": "password123",
  "nombre": "Miguel",
  "apellido": "Nefle"
}
```

Resultado esperado: `200 OK`
```json
{ "token": "eyJhbGciOiJIUzI1NiJ9..." }
```

Verificar en MySQL que la contraseña se guardó encriptada:
```sql
SELECT * FROM usuario;
```

El campo `password` debe mostrar el hash de BCrypt (empieza con `$2a$10$...`), nunca la contraseña original.

---

### Login correcto

**POST** `http://localhost:8080/auth/login`

```json
{
  "username": "miguel",
  "password": "password123"
}
```

Resultado esperado: `200 OK` con el token.

Copiar el token y pegarlo en [jwt.io](https://jwt.io). El payload decodificado muestra:
```json
{
  "sub": "miguel",
  "iat": 1234567890,
  "exp": 1234654290
}
```

`sub` es el username, `iat` es la fecha de emisión, `exp` es cuándo expira. El payload no está encriptado — está en Base64. Cualquiera puede decodificarlo. Lo que garantiza la seguridad es la firma, no el contenido.

---

### Login incorrecto

**POST** `http://localhost:8080/auth/login`

```json
{
  "username": "miguel",
  "password": "wrongpassword"
}
```

Resultado esperado: `403 Forbidden`

`authenticationManager.authenticate()` lanza `BadCredentialsException` porque BCrypt comparó los hashes y no coincidieron.

---

### Endpoint protegido sin token

**GET** `http://localhost:8080/productos`

Resultado esperado: `403 Forbidden`

`JwtFilter` no encontró token en el header, el `SecurityContext` quedó vacío, Spring Security bloqueó el request.

---

### Endpoint protegido con token válido

En Thunder Client ir a **Headers** y agregar:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**GET** `http://localhost:8080/productos`

Resultado esperado: `200 OK` con la lista de productos.

---

### Token modificado

Cambiar un carácter del token y volver a enviar el mismo request.

Resultado esperado: `403 Forbidden`

`JwtService` intentó verificar la firma contra la clave secreta y no coincidió. El `SecurityContext` quedó vacío.

---

## Resumen del flujo completo

| Request | Qué ocurre internamente | Resultado |
|---|---|---|
| `POST /auth/register` | `AuthService` crea el usuario con BCrypt → `JwtService` genera token | `200 OK` + `{ token }` |
| `POST /auth/login` (correcto) | `AuthenticationManager` verifica credenciales → `JwtService` genera token | `200 OK` + `{ token }` |
| `POST /auth/login` (incorrecto) | `AuthenticationManager` lanza `BadCredentialsException` | `403 Forbidden` |
| `GET /productos` (sin token) | `JwtFilter` no encuentra token → `SecurityContext` vacío → Spring Security bloquea | `403 Forbidden` |
| `GET /productos` (token válido) | `JwtFilter` valida → carga `SecurityContext` → `ProductoController` responde | `200 OK` |
| `GET /productos` (token modificado) | `JwtFilter` extrae → `JwtService` falla validación de firma → `SecurityContext` vacío | `403 Forbidden` |

---

## Opcional — dejar GET /productos público

Si se quiere que la consulta de productos funcione sin token (solo proteger POST, PUT y DELETE), reemplazar el bloque `authorizeHttpRequests` en `SecurityConfig`:

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/auth/**").permitAll()
    .requestMatchers(org.springframework.http.HttpMethod.GET, "/productos/**").permitAll()
    .requestMatchers(org.springframework.http.HttpMethod.GET, "/categorias/**").permitAll()
    .anyRequest().authenticated()
)
```

Esto deja libres los GET pero exige token para crear, modificar y eliminar.
