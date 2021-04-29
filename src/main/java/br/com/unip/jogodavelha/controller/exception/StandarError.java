package br.com.unip.jogodavelha.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class StandarError implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long timeStamp;

    private Integer status;

    private String error;

    private String message;

    private String path;

}
