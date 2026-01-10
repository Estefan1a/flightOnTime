package com.flightOnTime.flightOnTime.dto;

public record AirportStatsDTO(
        String aeropuerto,
        long totalPredicciones,
        long totalRetrasados,
        long totalCancelados,
        double porcentajeRetrasos,
        double porcentajeCancelaciones
) {}
