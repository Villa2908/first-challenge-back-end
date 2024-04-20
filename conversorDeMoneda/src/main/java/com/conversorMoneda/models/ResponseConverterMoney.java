package com.conversorMoneda.models;

import com.conversorMoneda.DTOs.ResponseConverterMoneyDTO;

public class ResponseConverterMoney {
    private String codigoBase, nombreBase, codidoCambio, nombreCambio;
    private double cambio, cambiado;

    public ResponseConverterMoney(ResponseConverterMoneyDTO responseConverter) {
        this.codigoBase = responseConverter.base_code();
        this.codidoCambio = responseConverter.target_code();
        this.cambio = responseConverter.conversion_rate();
        if (!(responseConverter.conversion_result() == null)){
            this.cambiado = responseConverter.conversion_result();
        }
    }

    public void setNombreBase(String nombreBase) {
        this.nombreBase = nombreBase;
    }

    public void setNombreCambio(String nombreCambio) {
        this.nombreCambio = nombreCambio;
    }
    public String obtenerTasa(){
        return "%s (%s)\n%s (%s)\nvalor en (%s): %.2f".formatted(codigoBase, nombreBase, codidoCambio, nombreCambio,codidoCambio, cambio);
    }
    @Override
    public String toString() {
        return "Moneda: " + codigoBase + " (%s)".formatted(nombreBase)
                + "\nMoneda a cambiar: " + codidoCambio + " (%s)".formatted(nombreCambio)
                +"\nCambio= " + cambio
                +"\nTotal= %.2f".formatted(cambiado);
    }
}
