package br.com.unip.jogodavelha.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

import static br.com.unip.jogodavelha.model.EnumPerfilCliente.CLIENTE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Cliente extends AbstractId {

    @NotEmpty
    @NotBlank
    private String nome;

    @Column(unique = true)
    private String nickname;

    @NotEmpty
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotEmpty
    @NotBlank
    private String senha;

    private String ip;

    private String porta;

    private String idServidor;

    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "perfil")
    @Enumerated(STRING)
    private Set<EnumPerfilCliente> enumPerfilClientes = new HashSet<>();

    public Cliente() {
        this.addPerfil(CLIENTE);
    }

    public void addPerfil(EnumPerfilCliente perfilCliente) {
        this.getEnumPerfilClientes().add(perfilCliente);
    }

}
