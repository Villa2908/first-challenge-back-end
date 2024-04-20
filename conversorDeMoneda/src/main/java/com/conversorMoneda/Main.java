package com.conversorMoneda;

import com.conversorMoneda.models.Historial;
import com.conversorMoneda.models.Moneda;
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
        int user = validarIngresoOpciones();
        String resultado;
        List<Historial> historial = new ArrayList<>();
        while (user != 7){
            switch (user){
                case 1:
                    //Consulta basica de monedas a la API solo muestra 6 tipos de monedas
                    imprimirMonedasBasicas();
                    String consulta = validarIngresoDatos();
                    resultado = moneda.obtenerValoresDefecto(consulta);
                    System.out.println(resultado);
                    historial.add(new Historial(resultado));
                    break;
                case 2:
                    //Consulta basica de tasa con las monedas basicas. 6 tipos de monedas igual
                    //Se obtiene valor de la tasa y nombres de monedas
                    imprimirMonedasBasicas();
                    System.out.println("Ingrese el codigo de la moneda que desea cambiar:");
                    String monedaACambiar = validarIngresoDatos();
                    System.out.println("Ingrese ahora el codigo de la moneda a tasar:");
                    String monedaTasa = validarIngresoDatos();
                    resultado = moneda.obtenerTasaMoneda(monedaACambiar, monedaTasa);
                    System.out.println(resultado);
                    historial.add(new Historial(resultado));
                    break;
                case 3:
                    //Obtienes la consulta con todas las monedas disponibles de la API
                    System.out.println("Ingrese el codigo de la moneda");
                    String monedaConsulta = validarIngresoDatos();
                    resultado = moneda.obtenerTodoValor(monedaConsulta);
                    System.out.println(resultado);
                    historial.add(new Historial(resultado));
                    break;
                case 4:
                    //Muestra en pantalla todas las monedas disponibles de la API
                    HashMap<String, String> listaMonedas = moneda.obtenerMonedas();
                    System.out.println("Estas monedas tenemos disponibles: ");
                    listaMonedas.forEach((tipo, nombre) -> {
                        System.out.println("%s (%s)".formatted(tipo, nombre));
                    });
                    historial.add(new Historial("Busqueda de monedas disponibles"));
                    break;
                case 5:
                    //Con ayuda de la API conviertes una cantidad deseada de moneda a otra
                    imprimirMonedasBasicas();
                    System.out.println("Ingrese el codigo de la moneda que desea cambiar:");
                    String monedaCambio = validarIngresoDatos();
                    System.out.println("Ingrese ahora la moneda a tasar:");
                    String monedaTasada = validarIngresoDatos();
                    double cantidad = validarIngresoMoneda();
                    resultado = moneda.convertirMoneda(monedaCambio, monedaTasada, cantidad);
                    System.out.println(resultado);
                    historial.add(new Historial(resultado));
                    break;
                case 6:
                    //Muestras el historial
                    if (historial.size() > 0){
                        historial.forEach(item -> System.out.println(item.toString()));
                        break;
                    } else {
                        System.out.println("Aun no hay registros disponibles");
                    }

            }
            System.out.println("Presione cualquier tecla para continuar...");
            entrada.nextLine();
            user = validarIngresoOpciones();
        }
        System.out.println("Muchas gracias por preferirnos! Nos vemos pronto");
    }
    public static int validarIngresoOpciones(){
        Scanner entrada = new Scanner(System.in);
        while (true){
            imprimirOpciones();
            String user = entrada.nextLine().trim();
            if(user.matches("^\\d{1}$")){
                if (Integer.valueOf(user) > 7){
                    System.out.println("El valor que ingresó no es admitido, intentelo de nuevo \n");
                }else {
                    return Integer.parseInt(user);
                }
            } else {
                System.out.println("El valor que ingresó no es admitido, intentelo de nuevo \n");
            }
        }
    }
    public static String validarIngresoDatos(){
        Scanner entrada = new Scanner(System.in);
        while (true){
            String user = entrada.nextLine().trim();
            if(user.matches("^[a-zA-Z]{3}$")){
                return user.toUpperCase();
            }else{
                System.out.println("El valor que ingresó no es admitido, intentelo de nuevo \n");
            }
        }
    }

    public static double validarIngresoMoneda(){
        Scanner entrada = new Scanner(System.in);
        System.out.println("Ingrese la cantidad que desea cambiar: ");
        while (true){
            String user = entrada.nextLine().trim();
            user = user.replaceFirst(",",".");
            if (user.matches("^\\d+(\\.\\d+)?$")) {
                return Double.valueOf(user);
            } else {
                System.out.println("Número inválido. Por favor, ingrese un número válido.");
            }
        }
    }
    public static void imprimirOpciones(){
        System.out.println("""
                ************************************************
                Que desea realizar:
                1 - Consulta una moneda
                2 - Consultar tasa de moneda
                3 - Consultar mas monedas
                4 - Consultar las monedas disponibles
                5 - Convertir cantidad de moneda
                6 - Consultar historial
                7 - Salir
                ************************************************
                """);
    }
    public static void imprimirMonedasBasicas(){
        System.out.println("""
                        Que moneda desea consultar:
                        ARS - Peso argentino
                        BOB - Boliviano boliviano
                        BRL - Real brasileño
                        CLP - Peso chileno
                        COP - Peso colombiano
                        USD - Dólar estadounidense
                        """);
    }
}