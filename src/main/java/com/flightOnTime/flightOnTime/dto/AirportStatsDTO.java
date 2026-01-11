package com.flightOnTime.flightOnTime.dto;

public record AirportStatsDTO(
        String aeropuerto,
        long totalPredicciones,
        long totalRetrasados,
        double porcentajeRetrasos
) {}
