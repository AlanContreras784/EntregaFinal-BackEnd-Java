package com.techlab.productos_ecologicos.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private String nombre;
    private String apellido;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private List<Carrito> carritos;

    // Devuelve los roles del usuario. Spring Security los usa para autorización.
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
            new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    // Los cuatro métodos siguientes devuelven true para simplificar el proyecto.
    // En producción se implementan con lógica real: cuenta bloqueada tras intentos
    // fallidos, email sin confirmar, credenciales vencidas, etc.
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() { return true; }
    
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() { return true; }
    
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    
    @JsonIgnore
    @Override
    public boolean isEnabled() { return true; }
}
