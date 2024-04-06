package com.conversorMoneda.models;

import com.conversorMoneda.DTOs.ResponseConsultMoneyDTO;
import com.conversorMoneda.DTOs.ResponseConverterMoneyDTO;
import com.conversorMoneda.DTOs.TipoMonedaDTO;
import com.conversorMoneda.connectionAPI.ConnetionAPI;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Clase que representa una moneda y proporciona métodos para interactuar con una API de tasas de cambio.
 */
public class Moneda implements TipoMoneda {
    // Atributos
    private String nombre;  // Nombre de la moneda
    private String simbolo; // Símbolo de la moneda
    /**
     * @param listaMonedaStr Una lista que se obtiene de la clase @TipoMonedaDTO
     * */
    public Moneda(List<String> listaMonedaStr){
        for (String item : listaMonedaStr){
            if(item.length() > 3){
                this.nombre = item;
            }else {
                this.simbolo = item;
            }
        }
    }
    public Moneda() {
    }

    /**
     * Principal metodo que se comunica con la API
     * @param endPoint es el endPoint de la API
     * @return String de la respuesta de la API
     * */
    private String obtenerValores(String endPoint) {
        ConnetionAPI conn = ConnetionAPI.getInstance();
        HttpClient cliente = conn.getCliente();
        String api = conn.getApi();
        String idConsumer = conn.getIdConsumer();
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(api + idConsumer + "/%s".formatted(endPoint))).build();
            HttpResponse<String> response = cliente.send(req, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    /**
     * Metodo de test para probar conexion y recepcion de JSON y API
     * */
    public String obtenerValoresDefecto(){
        return obtenerValores("latest/USD");
    }
    /**
     * Obtiene los codigos (3 caracteres "USD, ARS") de toda la API
     * @return Map<String, String> con los vales de los codigos y nombres respectivos
     * */
    public HashMap<String, String> obtenerMonedas(){
        Gson json = new Gson();
        HashMap<String, String> listaNombreMoneda = new HashMap<>();
        TipoMonedaDTO prueba = json.fromJson(obtenerValores("codes"), TipoMonedaDTO.class);

        for (List<String> item: prueba.supported_codes()){
            Moneda money = new Moneda(item);
            listaNombreMoneda.put(money.simbolo, money.nombre);
        }
        return listaNombreMoneda;
    }
    /**
    * @param monedaBuscada Codigo moneda que se busca conseguir tasa
     * @param monedaCambio Codigo moneda que se tasa
     * @return Valores de las monedas, con sus nombres y tasa
    * */
    public String obtenerValorMoneda(String monedaBuscada, String monedaCambio){
        ResponseConverterMoney tasaObtenida = obtenerTasaMoneda(monedaBuscada, monedaCambio);
        return tasaObtenida.obtenerTasa();
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + " - Simbolo: " + simbolo ;
    }
    /**
     * Metodo de la Interfaz @TipoMoneda
     * @param monedaAConvertir moneda que se desea converitr
     * @param monedaConvertida moneda que se desea tasar
     * @param cantidadACambiar cantidad de monedaAConvertir para tasar
     * @return Moneda convertida
     * */
    @Override
    public String convertirMoneda(String monedaAConvertir, String monedaConvertida, double cantidadACambiar) {
        ResponseConverterMoney tasaMoneda = obtenerTasaMoneda(monedaAConvertir, monedaConvertida);
        tasaMoneda.setCambiado(tasaMoneda.getCambio() * cantidadACambiar);
        return tasaMoneda.toString();
    }
    /**
     * Metodo principal para obtener conversion de moneda
     * @param monedaAConvertir codigo moneda que se quiere convertir
     * @param monedaConvertida codigo moneda que se tasa
     * @return Objeto ResponseConverterMoney para su manipulacion de datos
     * */
    @Override
    public ResponseConverterMoney obtenerTasaMoneda(String monedaAConvertir, String monedaConvertida) {
        ConnetionAPI conn = ConnetionAPI.getInstance();
        HttpClient cliente = conn.getCliente();
        String api = conn.getApi();
        String idConsumer = conn.getIdConsumer();
        Gson json = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(api + idConsumer + "/pair/%s/%s"
                            .formatted(monedaAConvertir,monedaConvertida)))
                    .build();
            HttpResponse<String> response = cliente.send(req, HttpResponse.BodyHandlers.ofString());
            ResponseConverterMoneyDTO respuestaConvertida = json.fromJson(response.body(), ResponseConverterMoneyDTO.class);
            ResponseConverterMoney converted = new ResponseConverterMoney(respuestaConvertida);
            Map<String, String> listaNombreMoneda = obtenerMonedas();
            for (String item: listaNombreMoneda.keySet()){
                if(item.equals(monedaAConvertir)){
                    converted.setNombreBase(listaNombreMoneda.get(item));
                } else if(item.equals(monedaConvertida)){
                    converted.setNombreCambio(listaNombreMoneda.get(item));
                }
            }
            return converted;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
