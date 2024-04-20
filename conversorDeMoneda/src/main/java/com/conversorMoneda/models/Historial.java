package com.conversorMoneda.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Historial {
    private String registro;
    private LocalDate fecha;
    private LocalTime hora;

    public Historial(String registro){
        this.registro = registro;
        fecha = LocalDate.now();
        hora = LocalTime.now();
    }

    @Override
    public String toString() {
        return  registro
                + "\nfecha: "
                + fecha.format(DateTimeFormatter.ofPattern("dd-MM-yy"))
                + "\nhora: "
                + hora.format(DateTimeFormatter.ofPattern("hh-mm-ss")) ;
    }
}
