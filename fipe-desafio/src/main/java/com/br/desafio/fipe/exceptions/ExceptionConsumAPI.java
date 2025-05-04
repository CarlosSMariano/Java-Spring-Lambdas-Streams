package com.br.desafio.fipe.exceptions;

public class ExceptionConsumAPI extends RuntimeException {
    public ExceptionConsumAPI(String message) {
        super(message);
    }

    public ExceptionConsumAPI(String message, Exception erro){
        super(message, erro);
    }
}
