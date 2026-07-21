package com.techlab.productos_ecologicos.auth;

import com.techlab.productos_ecologicos.dto.LoginRequestDTO;
import com.techlab.productos_ecologicos.dto.LoginResponseDTO;
import com.techlab.productos_ecologicos.dto.RegistroRequestDTO;
import com.techlab.productos_ecologicos.jwt.JwtService;
import com.techlab.productos_ecologicos.models.Role;
import com.techlab.productos_ecologicos.models.Usuario;
import com.techlab.productos_ecologicos.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
/**
 * Servicio encargado de la autenticación y registro de usuarios.
 *
 * Responsabilidades:
 *
 * - Validar credenciales mediante Spring Security.
 * - Crear usuarios nuevos.
 * - Encriptar contraseñas con BCrypt.
 * - Generar tokens JWT.
 *
 * No maneja respuestas HTTP.
 * Esa responsabilidad pertenece al AuthController.
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    /**
     * Autentica un usuario existente.
     *
     * Flujo:
     *
     * 1) Recibe username y password.
     * 2) Spring Security valida las credenciales.
     * 3) Busca el usuario autenticado.
     * 4) Genera un JWT.
     * 5) Devuelve la información al Controller.
     */
    public LoginResponseDTO login(LoginRequestDTO request) {
        // Activa la cadena interna de Spring Security.
        //
        // Internamente:
        // - Busca el usuario por username.
        // - Aplica BCrypt.
        // - Compara contraseña ingresada contra la almacenada.
        //
        // Si falla, Spring lanza una excepción de autenticación.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        // Recuperamos el usuario autenticado.
        //
        // UserDetails es la interfaz que utiliza Spring Security.
        Usuario usuario = usuarioRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado."));
        // Generamos el token JWT.
        String token = jwtService.generarToken(usuario);
        // Devolvemos la respuesta para el frontend.
       return new LoginResponseDTO(
                token,
                usuario.getUsername(),
                usuario.getRole().name()
        );
    }
    /**
     * Registra un nuevo usuario.
     *
     * Flujo:
     *
     * 1) Recibe datos del formulario.
     * 2) Encripta la contraseña.
     * 3) Guarda el usuario.
     * 4) Genera JWT automáticamente.
     */
    public LoginResponseDTO register(RegistroRequestDTO request) {
        // Construcción del usuario.
        //
        // La contraseña nunca se guarda en texto plano.
        // BCrypt genera un hash irreversible.
        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .role(Role.USER)
                .build();
        // Guarda el usuario en la base de datos.
        usuarioRepository.save(usuario);
        // Genera automáticamente la sesión.
        String token = jwtService.generarToken(usuario);
        return new LoginResponseDTO(
                token,
                usuario.getUsername(),
                usuario.getRole().name()
        );
    }
}