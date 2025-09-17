package com.fqlopes.demonstration.resources.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/*
    Classe responsável por tratar dos erros e limpar a mensagem,
    mantendo apenas as mensagens mais pertinentes
 */
@Getter
@Setter
public class FieldMessage implements Serializable {
    private static long serialVersionUID = 1L;

    private String fieldName;
    private String message;

    public FieldMessage() {
    }

    public FieldMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }
}
