package com.br.desafio.fipe.service;

import com.br.desafio.fipe.exceptions.ExceptionConvertJsos;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.List;

public class ConvertJson implements ICovertJson{
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T convertJson(String json, Class<T> cls) {
        try{
            return mapper.readValue(json, cls);
        } catch (JsonProcessingException e) {
            throw new ExceptionConvertJsos("Error to convert JSON: " + e);
        }
    }

    @Override
    public <T> List<T> convertList(String json, Class<T> cls) {
        try {
            CollectionType listType = mapper.getTypeFactory()
                    .constructCollectionType(List.class, cls);
            return mapper.readValue(json, listType);

        } catch (JsonProcessingException e) {
            throw new ExceptionConvertJsos("Error to convert list: " + e);
        }

    }
}
