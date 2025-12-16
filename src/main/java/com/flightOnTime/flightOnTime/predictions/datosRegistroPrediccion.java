package com.flightOnTime.flightOnTime.predictions;

import org.springframework.format.annotation.DateTimeFormat;

public record datosRegistroPrediccion(String aereolineas, String origen, String destino, DateTimeFormat fechaDePartida, Integer distanciaEnKm) {

}
