package br.com.unip.jogodavelha.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public abstract class AbstractIdDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
}
