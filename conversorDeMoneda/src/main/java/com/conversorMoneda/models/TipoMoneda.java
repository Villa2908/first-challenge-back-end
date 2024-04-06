package com.conversorMoneda.models;

public interface TipoMoneda {
    String convertirMoneda(String monedaAConvertir, String monedaConvertida, double cantidadConvertir);
    ResponseConverterMoney obtenerTasaMoneda(String monedaAConvertir, String monedaConvertida);
}
