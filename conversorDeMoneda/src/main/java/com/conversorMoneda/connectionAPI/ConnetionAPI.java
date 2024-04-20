package com.conversorMoneda.connectionAPI;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConnetionAPI {
    private static ConnetionAPI instance;
    private HttpClient cliente;
    private String api;
    private String idConsumer;

    private String response;

    private ConnetionAPI() {
        api = "https://v6.exchangerate-api.com/v6/";
        idConsumer = "3a9a7d9b6cc7ceb65b6b115b";
        cliente = HttpClient.newHttpClient();
        response = "";
    }

    /**
     * Unico metodo para obtener una instancia o objeto de esta clase
     * patron de diseño : SINGLETON
     * @return instance of ConnetionAPI
     * */
    public static ConnetionAPI getInstance(){
        if(instance == null){
            return new ConnetionAPI();
        }
        return instance;
    }
    /**
     * Metodo para consultar a la API
     * @param endpoint es el punto o direccion url de la API para obtener alguna respuesta en formato JSON
     * @return respuesta es en String listo para convertirlo en un objeto JSON
     * */
    private String consultaApi(String endpoint) throws IOException, InterruptedException {
        URI url = URI.create(api + idConsumer + "/%s".formatted(endpoint));
        HttpRequest request = HttpRequest.newBuilder().uri(url).build();
        HttpResponse<String> respuesta = cliente.send(request,HttpResponse.BodyHandlers.ofString());
        return respuesta.body();
    }
    /**
     * @param moneda punto de moneda que desea consultar
     * @param cambio moneda que se desea cambiar
     * @return response es en String listo para convertirlo en un objeto JSON
     * */
    public String consulta (String moneda, String cambio) throws IOException, InterruptedException {
        response = consultaApi("pair/%s/%s".formatted(moneda, cambio));
        return response;
    }
    /**
     * @param moneda punto de moneda que desea consultar
     * @return response es en String listo para convertirlo en un objeto JSON
     * */
    public String consulta (String moneda) throws IOException, InterruptedException {
        response = consultaApi("latest/%s".formatted(moneda));
        return response;
    }
    /**
     * @param moneda punto de moneda que desea consultar
     * @param cambio moneda que se desea cambiar
     * @param cantidad cantidad de moneda que desea cambiar
     * @return response es en String listo para convertirlo en un objeto JSON
     * */
    public String consulta (String moneda, String cambio, double cantidad) throws IOException, InterruptedException {
        response = consultaApi("pair/%s/%s/%f".formatted(moneda, cambio,cantidad));
        return response;
    }
    public String consultaMonedas() throws IOException, InterruptedException {
        response = consultaApi("codes");
        return response;
    }
    public String getResponse() {
        return response;
    }
}
