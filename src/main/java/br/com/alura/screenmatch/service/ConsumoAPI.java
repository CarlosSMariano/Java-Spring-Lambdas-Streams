package br.com.alura.screenmatch.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {
    /**
     * Método para obter dados de uma URL (normalmente um JSON) via requisição HTTP GET
     * param e - URL do endpoint que será consultado
     * return String com o conteúdo da resposta (geralmente um JSON)
     * throws RuntimeException se ocorrer erro na comunicação
     */
    public String obterDados(String endereco){
        // Cria um novo cliente HTTP para fazer a requisição
        HttpClient client = HttpClient.newHttpClient();

        // Constrói a requisição HTTP GET para a URL especificada
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco)) // Define a URL da requisição
                .build();   //  Finaliza a construção do objeto HttpRequest

        // Variável que irá armazenar a resposta HTTP
        HttpResponse<String> response = null;

        try{
            // Envia a requisição e recebe a resposta
            // HttpResponse.BodyHandlers.ofString() indica que queremos o corpo como String
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
        }catch (IOException e){
            // Converte exceções de I/O (erros de rede, etc) em RuntimeException
            throw new RuntimeException(e);
        }catch (InterruptedException e){
            // Converte execeção de interrupção em RuntimeException
            throw new RuntimeException(e);
        }
        // Extrai o corpo da resposta como String
        String json = response.body();

        // Reorna o contrúdo obtido
        return json;
    }

}
