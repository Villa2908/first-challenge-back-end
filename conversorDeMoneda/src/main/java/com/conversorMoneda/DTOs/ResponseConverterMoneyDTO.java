package com.conversorMoneda.DTOs;

public record ResponseConverterMoneyDTO(String base_code, String target_code, Double conversion_rate) {
}
