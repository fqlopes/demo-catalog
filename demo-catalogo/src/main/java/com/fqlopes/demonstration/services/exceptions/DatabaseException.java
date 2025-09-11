package com.fqlopes.demonstration.services.exceptions;


//Esta classe tem como objetivo lidar com os as exceções.
//A resposta sobre os erros HTTP estarão no pacote de recursos (resources)/controladora


public class DatabaseException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public DatabaseException(String message){
        super(message);
    }

}
