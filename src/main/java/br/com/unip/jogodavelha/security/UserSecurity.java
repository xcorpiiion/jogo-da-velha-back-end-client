package br.com.unip.jogodavelha.security;

import br.com.unip.jogodavelha.model.EnumPerfilCliente;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Getter
@NoArgsConstructor
public class UserSecurity implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String email;

    private String senha;

    private Collection<? extends GrantedAuthority> authorities;

    public UserSecurity(Long id, String email, String senha, Set<EnumPerfilCliente> perfilClientes) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.authorities = perfilClientes.stream().
                map(enumPerfilCliente -> new SimpleGrantedAuthority(enumPerfilCliente.getPerfil())).collect(toList());
    }

    @Override
    public String getPassword() {
        return this.getSenha();
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean hasRole(EnumPerfilCliente enumPerfilCliente) {
        return this.getAuthorities().contains(new SimpleGrantedAuthority(enumPerfilCliente.getPerfil()));
    }
}
