package com.fqlopes.demonstration.resources.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;


@Getter
@Setter
public class StandardError implements Serializable {
    private static final long serialVersionUID = 1L;

    //Campos
    private Instant timestamp; //Quando ocorreu
    private Integer status; // Código HTML
    private String error; // Tipo de erro
    private String message; // Mensagem do erro
    private String path; // Onde aconteceu

    //constructor
    public StandardError (){

    }

}
