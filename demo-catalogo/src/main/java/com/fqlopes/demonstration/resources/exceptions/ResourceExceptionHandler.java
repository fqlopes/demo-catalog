package com.fqlopes.demonstration.resources.exceptions;


import com.fqlopes.demonstration.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

//A anotação @ControllerAdvice aponta a classe como responsável por tratar erros e exceções
@ControllerAdvice
public class ResourceExceptionHandler {

    // O método abaixo intercepta nosso controlador para tratar da exceção

    @ExceptionHandler(ResourceNotFoundException.class) //Identificando o tipo de exceção a ser utilizada
    public ResponseEntity<StandardError> entityNotFound (ResourceNotFoundException e, HttpServletRequest request){
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setError("Algo de errado não está certo!");
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

    }
}
