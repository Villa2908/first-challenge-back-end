package com.conversorMoneda.connectionAPI;

import java.net.http.HttpClient;

public class ConnetionAPI {
    private static ConnetionAPI instance;
    private HttpClient cliente;
    private String api;
    private String idConsumer;

    public ConnetionAPI() {
        api = "https://v6.exchangerate-api.com/v6/";
        idConsumer = "3a9a7d9b6cc7ceb65b6b115b";
        cliente = HttpClient.newHttpClient();
    }

    public static ConnetionAPI getInstance(){
        if(instance == null){
            return new ConnetionAPI();
        }
        return instance;
    }

    public HttpClient getCliente() {
        return cliente;
    }

    public String getApi() {
        return api;
    }

    public String getIdConsumer() {
        return idConsumer;
    }
}
