package com.conversorMoneda.models;

import com.conversorMoneda.DTOs.ResponseConsultMoneyDTO;
import com.conversorMoneda.DTOs.ResponseConverterMoneyDTO;
import com.conversorMoneda.DTOs.TipoMonedaDTO;
import com.conversorMoneda.connectionAPI.ConnetionAPI;
import com.google.gson.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author VILLA
 * Principal para acceder a la app.
 * Convertidor de moneda a tiempo real con uso de API
 * @apiNote https://www.exchangerate-api.com/
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

    public Moneda(String nombre, String simbolo) {
        this.nombre = nombre;
        this.simbolo = simbolo;
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
     * @param sigMonedaConvertir siglas de 3 caracteres para buscar tasa de monedas
     * ARS - Peso argentino
     * BOB - Boliviano boliviano
     * BRL - Real brasileño
     * CLP - Peso chileno
     * COP - Peso colombiano
     * USD - Dólar estadounidense
     * */
    public String obtenerValoresDefecto(String sigMonedaConvertir){
        Gson json = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        ResponseConsultMoneyDTO DTO = json.fromJson(obtenerValores("latest/" + sigMonedaConvertir), ResponseConsultMoneyDTO.class);
        ResponseConsultMoney valoresDefecto = new ResponseConsultMoney(DTO);
        HashMap<String,Double> monedasDefecto = new HashMap<>();
        valoresDefecto.getConversionValor().forEach((sigla, valor) ->{
            switch (sigla){
                case "ARS":
                case "BOB":
                case "BRL":
                case "CLP":
                case "COP":
                case "USD":
                    monedasDefecto.put(sigla, valor);
                    break;
            }
        });
        valoresDefecto.setConversionValor(monedasDefecto);
        return valoresDefecto.toString();

    }

    public String obtenerTodo(String sigMonedaTasar){
        Gson json = new Gson();
        ResponseConsultMoneyDTO DTO = json.fromJson(obtenerValores("latest/" + sigMonedaTasar), ResponseConsultMoneyDTO.class);
        ResponseConsultMoney valorTodas = new ResponseConsultMoney(DTO);
        return valorTodas.toString();
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
     * @return Valores de las monedas, con sus nombres y tasa
    * */
    public String obtenerValoresMonedas(String monedaBuscada){
        Gson json = new Gson();
        String consulta = obtenerValores("latest/"+monedaBuscada);
        ResponseConsultMoneyDTO DTO = json.fromJson(consulta, ResponseConsultMoneyDTO.class);
        ResponseConsultMoney response = new ResponseConsultMoney(DTO);
        return response.toString();
    }

    @Override
    public String toString() {
        return "Siglas: " + simbolo + " - " + nombre ;
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
    private ResponseConverterMoney obtenerTasaMoneda(String monedaAConvertir, String monedaConvertida) {
        Gson json = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        try {
            String consulta = obtenerValores("pair/%s/%s".formatted(monedaAConvertir, monedaConvertida));
            ResponseConverterMoneyDTO respuestaConvertida = json.fromJson(consulta, ResponseConverterMoneyDTO.class);
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
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
