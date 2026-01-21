package com.flightOnTime.flightOnTime.dto;

public record WeatherInfo(

        //Temperatura estimada en el aeropuerto de destino (°C).
        double temperatura,

        //Velocidad del viento estimada (km/h).
        double viento
) {}
