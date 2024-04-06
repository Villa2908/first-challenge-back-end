package com.conversorMoneda;

import com.conversorMoneda.DTOs.TipoMonedaDTO;
import com.conversorMoneda.models.Moneda;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
/**
 * @author VILLA
 * Principal para acceder a la app.
 * Convertidor de moneda a tiempo real con uso de API
 * @apiNote https://www.exchangerate-api.com/
 * */

public class Main {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        Moneda moneda = new Moneda();
        HashMap<String, String> listaMoneda = moneda.obtenerMonedas();
        imprimirOpciones();
        int user = entrada.nextInt();
        List<String> historial = new ArrayList<>();
        try{
            while (user < 4){
                String resultado;
                Scanner monedaELegir = new Scanner(System.in);
                Scanner cantidadACambiar = new Scanner(System.in);
                switch (user) {
                    case 1:
                        imprimirMonedas(listaMoneda);
                        String monedaAConsultar = monedaELegir.nextLine().toUpperCase();
                        System.out.println("Ahora escriba la moneda a consultar: ");
                        String monedaConvertida = monedaELegir.nextLine().toUpperCase();
                        resultado = moneda.obtenerValorMoneda(monedaAConsultar, monedaConvertida);
                        System.out.println(resultado);
                        historial.add(resultado + "\n"+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy"))
                                + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                        break;
                    case 2:
                        imprimirMonedas(listaMoneda);
                        String monedaAConvertir = monedaELegir.nextLine().toUpperCase();
                        System.out.println("Cuanto desea cambiar: ");
                        double cantidadCambio = cantidadACambiar.nextDouble();
                        System.out.println("Ahora escribe la moneda a convertir: ");
                        monedaConvertida = monedaELegir.nextLine().toUpperCase();
                        resultado = moneda.convertirMoneda(monedaAConvertir, monedaConvertida, cantidadCambio);
                        System.out.println(resultado);
                        historial.add(resultado + "\n"+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy"))
                                + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                        break;
                    case 3:
                        historial.forEach(item -> System.out.println(item + "\n"));
                        break;
                }
                imprimirOpciones();
                user = entrada.nextInt();
            }
        }catch (Exception e){
            System.out.println("Vaya, ha ocurrido un error: " + e.getCause());
        }
        System.out.println("Muchas gracias por preferirnos!");
    }
    public static void imprimirMonedas(Map<String,String> listaMoneda){
        System.out.println("Monedas disponibles: ");
        listaMoneda.forEach((simb, nomb) -> System.out.println("%s = %s".formatted(simb,nomb)));
        System.out.println("Elija el codigo de la moneda a cambiar (USD,ARS,PEN):");
    }
    public static void imprimirOpciones(){
        System.out.println("""
                
                ************************************************
                Que desea realizar:
                1 - Consulta una moneda
                2 - Convertir una moneda a otra
                3 - Consultar historial
                4 - Salir
                ************************************************
                
                """);
    }
}