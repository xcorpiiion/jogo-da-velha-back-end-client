package br.com.unip.jogodavelha.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumPerfilCliente {

    ADMIN(1, "ROLE_ADMIN"),
    CLIENTE(2, "ROLE_CLIENTE");

    private final int codigo;

    private final String perfil;

    public static EnumPerfilCliente toEnum(Integer codigo) {
        for (EnumPerfilCliente tipoCliente : EnumPerfilCliente.values()) {
            if (codigo.equals(tipoCliente.getCodigo())) {
                return tipoCliente;
            }
        }
        throw new IllegalArgumentException("Codigo Invalid: " + codigo);
    }
}
