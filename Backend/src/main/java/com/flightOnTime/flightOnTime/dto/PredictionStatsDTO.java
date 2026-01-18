package com.flightOnTime.flightOnTime.dto;

/**
 * DTO que representa estadísticas globales de las predicciones realizadas.
 *
 * Proporciona una visión general del comportamiento de los vuelos procesados
 * por el sistema, incluyendo cantidades absolutas y métricas porcentuales.
 *
 * Usado en:
 * - Respuesta del endpoint GET /stats
 *
 * Métricas incluidas:
 * - totalPredicciones: cantidad total de vuelos evaluados
 * - totalPuntuales: vuelos predichos como a tiempo
 * - totalRetrasados: vuelos predichos como retrasados
 * - porcentajeRetrasados: proporción de vuelos retrasados sobre el total
 */
public record PredictionStatsDTO(

        /** Cantidad total de predicciones generadas por el sistema */
        long totalPredicciones,

        /** Cantidad de vuelos predichos como puntuales */
        long totalPuntuales,

        /** Cantidad de vuelos predichos como retrasados */
        long totalRetrasados,

        /** Porcentaje de vuelos retrasados sobre el total (0 a 100) */
        double porcentajeRetrasados
) {
}
