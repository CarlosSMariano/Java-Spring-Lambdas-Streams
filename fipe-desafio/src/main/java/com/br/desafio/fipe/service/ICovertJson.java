package com.br.desafio.fipe.service;

import java.util.List;

public interface ICovertJson {
    <T> T convertJson(String json, Class<T> cls);
    <T> List<T> convertList(String json, Class<T> cls);
}
