package com.conversorMoneda;

import com.conversorMoneda.DTOs.TipoMonedaDTO;
import com.conversorMoneda.models.Moneda;
import com.google.gson.*;

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
        List<String> historial = new ArrayList<>();
        int eleccion = validarIngreso();
        while (eleccion != 5)
            switch (eleccion){
                case 1:
                    listaMoneda.forEach((simb, nomb)->{
                        Moneda monedaDisponible = new Moneda(nomb, simb);
                        System.out.println(monedaDisponible);
                    });
                    System.out.println("Ingrese las siglas de la moneda a consultar: ");
                    String user = entrada.nextLine().toUpperCase().trim();
                    while (user.length() < 3 || user.length() > 3){
                        System.out.println("Lo siento, ingresó un codigo incorrecto. Intentelo de nuevo");
                        user = entrada.nextLine().trim();
                    }
                    String resultado = moneda.obtenerValoresDefecto(user) + "\n"
                            + LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")) + " "
                            + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy"));
                    historial.add(resultado);
                    System.out.println(resultado);
                    eleccion = validarIngreso();
                    break;
                case 2:
                    listaMoneda.forEach((simb, nomb)->{
                        Moneda monedaDisponible = new Moneda(nomb, simb);
                        System.out.println(monedaDisponible);
                    });
                    System.out.println("Ingrese las siglas de la moneda a cambiar: ");
                    String monedaCambiar = entrada.nextLine().toUpperCase().trim();

                    System.out.println("Ingrese la cantidad a convertir: ");
                    double cantidad = entrada.nextDouble();

                    System.out.println("Ingrese las siglas de la moneda a convertir: ");
                    String monedaConvertida = entrada.nextLine().toUpperCase().trim();

                    while (monedaCambiar.length() < 3 || monedaCambiar.length() > 3){
                        System.out.println("Lo siento, ingresó un codigo incorrecto. Intentelo de nuevo");
                        monedaCambiar = entrada.nextLine().trim();
                    }
                    while (monedaConvertida.length() < 3 || monedaConvertida.length() > 3){
                        System.out.println("Lo siento, ingresó un codigo incorrecto. Intentelo de nuevo");
                        monedaConvertida = entrada.nextLine().trim();
                    }
                    String convertida = moneda.convertirMoneda(monedaCambiar, monedaConvertida,cantidad)  + "\n"
                            + LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")) + " "
                            + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy"));
                    historial.add(convertida);
                    System.out.println(convertida);
                    eleccion = validarIngreso();
                    break;
                case 3:
                    listaMoneda.forEach((simb, nomb)->{
                        Moneda monedaDisponible = new Moneda(nomb, simb);
                        System.out.println(monedaDisponible);
                    });
                    System.out.println("Ingrese las siglas de la moneda a consultar: ");
                    String monedaConsulta = entrada.nextLine().toUpperCase().trim();
                    while (monedaConsulta.length() < 3 || monedaConsulta.length() > 3){
                        System.out.println("Lo siento, ingresó un codigo incorrecto. Intentelo de nuevo");
                        monedaConsulta = entrada.nextLine().trim();
                    }
                    String resultadoConsulta = moneda.obtenerTodo(monedaConsulta) + "\n"
                            + LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")) + " "
                            + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy"));
                    historial.add(resultadoConsulta);
                    System.out.println(resultadoConsulta);
                    eleccion = validarIngreso();
                    break;
                case 4:
                    System.out.println(historial);
                    eleccion = validarIngreso();
                    break;
            }
    }
    public static int validarIngreso(){
        Scanner entrada = new Scanner(System.in);
        while (true){
            imprimirOpciones();
            String user = entrada.nextLine().trim();
            if(user.isEmpty() || Character.isLetter(user.charAt(0)) || user.length() > 1){
                System.out.println("El valor que ingresó no es admitido, intentelo de nuevo \n");
            } else if(Character.isDigit(user.charAt(0))){
                if(Integer.parseInt(user) > 6){
                    System.out.println("El valor que ingresó no es admitido, intentelo de nuevo \n");
                } else {
                    return Integer.parseInt(user);
                }
            }
        }
    }
    public static void imprimirOpciones(){
        System.out.println("""
                ************************************************
                Que desea realizar:
                1 - Consulta una moneda
                2 - Convertir una moneda a otra
                3 - Consultar mas monedas
                4 - Consultar historial
                5 - Salir
                ************************************************
                """);
    }
}