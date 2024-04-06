package com.conversorMoneda.models;

import com.conversorMoneda.DTOs.ResponseConsultMoneyDTO;

import java.util.HashMap;

public class ResponseConsultMoney {
    String monedaBase;
    HashMap<String, Double> conversionValor;

    public ResponseConsultMoney(ResponseConsultMoneyDTO responseDTO){
        this.monedaBase = responseDTO.base_code();
        this.conversionValor = responseDTO.conversion_rates();
    }

    @Override
    public String toString() {
        return "Moneda= " + monedaBase + "\nValores de monedas= " + conversionValor;
    }
}
