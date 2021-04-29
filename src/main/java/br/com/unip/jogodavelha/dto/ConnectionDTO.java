package br.com.unip.jogodavelha.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class ConnectionDTO extends AbstractIdDTO {

    private String ip;

    private Integer port;

    private String playerName;

    private String email;

}
