package com.br.desafio.fipe.service;

import com.br.desafio.fipe.exceptions.ExceptionConsumAPI;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class APIConsum {

    private static HttpClient client = HttpClient.newHttpClient();

    public String getData(String url){

        if (url == null || url.isBlank()){
            throw new ExceptionConsumAPI("Empty address");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();

        } catch (IOException e) {
            throw new ExceptionConsumAPI("Error to output and input: " + e);
        } catch (InterruptedException e) {
            throw new ExceptionConsumAPI("interrupted communication: " + e);
        }
    }
}
