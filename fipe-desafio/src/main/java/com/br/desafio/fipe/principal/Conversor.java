package com.br.desafio.fipe.principal;

import com.br.desafio.fipe.service.APIConsum;
import com.br.desafio.fipe.service.ConvertJson;

import java.util.List;

public class Conversor {
    private APIConsum apiConsum = new APIConsum();
    private final ConvertJson convertJson = new ConvertJson();

    private String api(String url) {
        return apiConsum.getData(url);
    }

    public <T> List<T> translateListJson(String url, Class<T> cls){
         var json = api(url);
         return convertJson.convertList(json, cls);
    }

    public <T> T translateJson(String url, Class<T> cls){
         var json = api(url);
         return convertJson.convertJson(json, cls);
    }
}
