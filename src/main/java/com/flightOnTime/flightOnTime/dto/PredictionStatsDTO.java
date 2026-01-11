package com.flightOnTime.flightOnTime.dto;

public record PredictionStatsDTO(
        long totalPredicciones,
        long totalPuntuales,
        long totalRetrasados,
        double porcentajeRetrasados
) {
}
