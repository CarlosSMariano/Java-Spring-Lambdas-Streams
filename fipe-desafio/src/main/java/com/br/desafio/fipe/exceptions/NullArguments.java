package com.br.desafio.fipe.exceptions;

public class NullArguments extends RuntimeException {
    public NullArguments(String message) {
        super(message);
    }

    public NullArguments(String message, Exception e){
        super(message,  e);
    }
}
