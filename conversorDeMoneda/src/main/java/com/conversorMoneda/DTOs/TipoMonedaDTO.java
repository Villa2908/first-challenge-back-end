package com.conversorMoneda.DTOs;


import java.util.LinkedList;
import java.util.List;

public record TipoMonedaDTO(LinkedList<List<String>> supported_codes) {
}
