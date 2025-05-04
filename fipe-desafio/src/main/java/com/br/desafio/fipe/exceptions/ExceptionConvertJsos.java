package com.br.desafio.fipe.exceptions;

public class ExceptionConvertJsos extends RuntimeException {
    public ExceptionConvertJsos(String message) {
        super(message);
    }
    public ExceptionConvertJsos(String message, Exception e) {
        super(message, e);
    }
}
