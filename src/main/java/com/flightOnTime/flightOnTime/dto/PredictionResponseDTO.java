package com.flightOnTime.flightOnTime.dto;

/**
 * DTO que representa la respuesta de una predicción de vuelo.
 *
 * Contiene el resultado calculado por el modelo predictivo,
 * indicando el estado estimado del vuelo y la probabilidad asociada.
 *
 * Usado en:
 * - Respuesta del endpoint POST /predict
 *
 * Convenciones:
 * - prevision: estado estimado del vuelo (ej: "A TIEMPO", "RETRASADO")
 * - probabilidad: valor entre 0 y 1 que indica el nivel de confianza del modelo
 */
public record PredictionResponseDTO(

        /** Estado estimado del vuelo según el modelo predictivo */
        String prevision,

        /** Probabilidad asociada a la predicción (0.0 a 1.0) */
        Double probabilidad
) {}
