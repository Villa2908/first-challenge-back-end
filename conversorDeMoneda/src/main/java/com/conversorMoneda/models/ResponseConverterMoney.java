package com.conversorMoneda.models;

import com.conversorMoneda.DTOs.ResponseConverterMoneyDTO;

public class ResponseConverterMoney {
    private String codigoBase, nombreBase, codidoCambio, nombreCambio;
    private double cambio, cambiado;

    public ResponseConverterMoney(ResponseConverterMoneyDTO responseConverter) {
        this.codigoBase = responseConverter.base_code();
        this.codidoCambio = responseConverter.target_code();
        this.cambio = responseConverter.conversion_rate();
    }

    public void setNombreBase(String nombreBase) {
        this.nombreBase = nombreBase;
    }

    public void setNombreCambio(String nombreCambio) {
        this.nombreCambio = nombreCambio;
    }

    public double getCambio() {
        return cambio;
    }

    public void setCambiado(double cambiado) {
        this.cambiado = cambiado;
    }

    @Override
    public String toString() {
        return "Moneda: " + codigoBase + " (%s)".formatted(nombreBase)
                + "\nMoneda a cambiar: " + codidoCambio + " (%s)".formatted(nombreCambio)
                +"\nCambio= " + cambio
                +"\nTotal= " + cambiado;
    }
    public String obtenerTasa(){
        return "Moneda: " + codigoBase + " (%s)".formatted(nombreBase)
                + "\nMoneda a cambiar: " + codidoCambio + " (%s)".formatted(nombreCambio)
                +"\nCambio= " + cambio;
    }
}
