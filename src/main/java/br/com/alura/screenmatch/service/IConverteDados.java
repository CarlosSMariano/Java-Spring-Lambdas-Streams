package br.com.alura.screenmatch.service;

public interface IConverteDados {
    //generico <T> T
    <T> T obterDados(String json, Class<T> classe);
}
