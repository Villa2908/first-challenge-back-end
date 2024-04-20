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
public class Moneda implements TipoMoneda{
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
     * @param sigMonedaConvertir siglas de 3 caracteres para buscar tasa de monedas y solo muestra las siguientes:
     * ARS - Peso argentino
     * BOB - Boliviano boliviano
     * BRL - Real brasileño
     * CLP - Peso chileno
     * COP - Peso colombiano
     * USD - Dólar estadounidense
     * */
    public String obtenerValoresDefecto(String sigMonedaConvertir){
        Gson json = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
        ConnetionAPI conn = ConnetionAPI.getInstance();
        try {
            if (conn.getResponse().isEmpty() || conn.getResponse().contains("target_code") || conn.getResponse().contains("supported_codes")){

                ResponseConsultMoneyDTO DTO = json.fromJson(conn.consulta(sigMonedaConvertir), ResponseConsultMoneyDTO.class);
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
                return valoresDefecto.getResult();
            } else {
                System.out.println("Ya existo en memoria y obtienes info de mi sin consultar a la api");
                ResponseConsultMoneyDTO dto = json.fromJson(conn.getResponse(), ResponseConsultMoneyDTO.class);
                ResponseConsultMoney valoresDefecto = new ResponseConsultMoney(dto);
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
                return valoresDefecto.getResult();
            }
        } catch (IOException | InterruptedException e ){
            System.out.println("Verifique su conexion a internet o sus consultas, ha ocurrido un error.");
        }
        return null;
    }

    public String obtenerTodoValor(String sigMonedaTasar){
        Gson json = new Gson();
        try{
            ResponseConsultMoneyDTO DTO = json.fromJson(ConnetionAPI.getInstance().consulta(sigMonedaTasar), ResponseConsultMoneyDTO.class);
            ResponseConsultMoney valorTodas = new ResponseConsultMoney(DTO);
            return valorTodas.getResult();
        } catch (IOException | InterruptedException e){
            System.out.println("Verifique su conexion a internet o sus consultas, ha ocurrido un error.");
        }
        return null;
    }
    /**
     * Obtiene los codigos (3 caracteres "USD, ARS") de toda la API
     * @return Map<String, String> con los vales de los codigos y nombres respectivos
     * */
    public HashMap<String, String> obtenerMonedas() {
        Gson json = new Gson();
        HashMap<String, String> listaNombreMoneda = new HashMap<>();
        try {
            TipoMonedaDTO prueba = json.fromJson(ConnetionAPI.getInstance().consultaMonedas(), TipoMonedaDTO.class);

            for (List<String> item : prueba.supported_codes()) {
                Moneda money = new Moneda(item);
                listaNombreMoneda.put(money.simbolo, money.nombre);
            }
            return listaNombreMoneda;
        } catch (IOException | InterruptedException e) {
            System.out.println("Verifique su conexion a internet o sus consultas, ha ocurrido un error.");
        }
        return null;
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
     * @return Resultado de la moneda convertida
     * */
    @Override
    public String convertirMoneda(String monedaAConvertir, String monedaConvertida, double cantidadACambiar) {
        try{
            Gson json = new Gson();
            String consulta = ConnetionAPI.getInstance().consulta(monedaAConvertir, monedaConvertida, cantidadACambiar);
            ResponseConverterMoneyDTO dto = json.fromJson(consulta, ResponseConverterMoneyDTO.class);
            ResponseConverterMoney result = new ResponseConverterMoney(dto);
            //Se obtienen los nombres de las monedas con otra consulta.
            //Se verifica el codigo y se coloca su nombre respectivo
            HashMap<String, String> nombreMonedas = obtenerMonedas();
            result.setNombreCambio(nombreMonedas.get(monedaConvertida.toUpperCase()));
            result.setNombreBase(nombreMonedas.get(monedaAConvertir.toUpperCase()));
            return result.toString();
        }
        catch (IOException | InterruptedException e){
            System.out.println("Verifique su conexion a internet o sus consultas, ha ocurrido un error.");
        }
        return null;
    }

    /**
     * Metodo principal para obtener conversion de moneda
     * @param monedaAConvertir codigo moneda que se quiere convertir
     * @param monedaConvertida codigo moneda que se tasa
     * @return Objeto ResponseConverterMoney para su manipulacion de datos
     * */
    public String obtenerTasaMoneda(String monedaAConvertir, String monedaConvertida) {
        Gson json = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        try {
            String consulta = ConnetionAPI.getInstance().consulta(monedaAConvertir, monedaConvertida);
            ResponseConverterMoneyDTO respuestaConvertida = json.fromJson(consulta, ResponseConverterMoneyDTO.class);
            ResponseConverterMoney converted = new ResponseConverterMoney(respuestaConvertida);
            //Se obtienen los nombres de las monedas con otra consulta.
            //Se verifica el codigo y se coloca su nombre respectivo
            Map<String, String> listaNombreMoneda = obtenerMonedas();
            converted.setNombreBase(listaNombreMoneda.get(monedaAConvertir.toUpperCase()));
            converted.setNombreCambio(listaNombreMoneda.get(monedaConvertida.toUpperCase()));

            return converted.obtenerTasa();
        } catch (IOException | InterruptedException e) {
            System.out.println("Verifique su conexion a internet o sus consultas, ha ocurrido un error.");
        }
        return null;
    }
}
