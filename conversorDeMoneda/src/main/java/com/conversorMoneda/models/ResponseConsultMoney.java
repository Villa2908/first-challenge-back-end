package com.conversorMoneda.models;

import com.conversorMoneda.DTOs.ResponseConsultMoneyDTO;

import java.util.HashMap;

public class ResponseConsultMoney {
    private String monedaBase;
    private HashMap<String, Double> conversionValor;

    public ResponseConsultMoney(ResponseConsultMoneyDTO responseDTO){
        this.monedaBase = responseDTO.base_code();
        this.conversionValor = responseDTO.conversion_rates();
    }

    public HashMap<String, Double> getConversionValor() {
        return conversionValor;
    }

    public void setConversionValor(HashMap<String, Double> conversionValor) {
        this.conversionValor = conversionValor;
    }

    @Override
    public String toString() {
        return "Moneda= " + monedaBase + "\nValores de monedas= " + conversionValor;
    }
}
