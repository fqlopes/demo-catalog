package com.fqlopes.demonstration.resources.exceptions;


import com.fqlopes.demonstration.services.exceptions.DatabaseException;
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
        var status = HttpStatus.NOT_FOUND.value();
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(status);
        error.setError("Algo de errado não está certo!");
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> database (DatabaseException e, HttpServletRequest request){
        var status = HttpStatus.BAD_REQUEST.value();

        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(status);
        error.setError("Database Exception");
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }
}
