package br.com.unip.jogodavelha.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class CredenciaisDTO extends AbstractIdDTO {

    private String email;

    private String senha;


}
