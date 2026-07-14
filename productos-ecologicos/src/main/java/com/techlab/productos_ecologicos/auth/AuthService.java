package com.techlab.productos_ecologicos.auth;

import com.techlab.productos_ecologicos.jwt.JwtService;
import com.techlab.productos_ecologicos.models.Role;
import com.techlab.productos_ecologicos.models.Usuario;
import com.techlab.productos_ecologicos.repository.UsuarioRepository;
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
