package com.conversorMoneda.DTOs;

import java.util.HashMap;

public record ResponseConsultMoneyDTO(String base_code, HashMap<String, Double> conversion_rates) {
}
