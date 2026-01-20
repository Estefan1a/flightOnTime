package com.flightOnTime.flightOnTime.dto;

/**
 * DTO que representa estadísticas agregadas de vuelos por aeropuerto de origen.
 *
 * Expone información analítica sobre el nivel de retrasos
 * para cada aeropuerto.
 *
 * Usado en:
 * - GET /stats/airports/top
 */
public record AirportStatsDTO(

        /** Código o nombre del aeropuerto de origen */
        String aeropuerto,

        /** Cantidad total de predicciones realizadas para el aeropuerto */
        long totalPredicciones,

        /** Cantidad total de vuelos clasificados como retrasados */
        long totalRetrasados,

        /** Porcentaje de vuelos retrasados sobre el total (0.0 a 1.0) */
        double porcentajeRetrasos
) {}

