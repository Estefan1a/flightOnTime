package com.flightOnTime.flightOnTime.dto;

import java.time.LocalDateTime;

import java.time.LocalDateTime;

/**
 * DTO que representa un registro histórico de una predicción de vuelo.
 *
 * <p>
 * Se utiliza para exponer información de predicciones ya realizadas,
 * permitiendo auditoría, trazabilidad y visualización de resultados
 * pasados del modelo predictivo.
 * </p>
 *
 * <p>
 * Este DTO suele emplearse en:
 * <ul>
 *     <li>Listados de historial de predicciones</li>
 *     <li>Consultas por usuario o por rango de fechas</li>
 *     <li>Visualización de métricas históricas</li>
 * </ul>
 * </p>
 *
 * <h3>Convenciones</h3>
 * <ul>
 *     <li>{@code prevision}: estado estimado del vuelo (ej: "A TIEMPO", "RETRASADO")</li>
 *     <li>{@code probabilidad}: nivel de confianza del modelo, valor entre 0.0 y 1.0</li>
 *     <li>{@code fechaPartida}: fecha y hora programada del vuelo</li>
 *     <li>{@code fechaPrediccion}: momento en el que se ejecutó la predicción</li>
 * </ul>
 *
 * <p>
 * No contiene lógica de negocio ni referencias a entidades JPA.
 * Está diseñado exclusivamente para intercambio de datos hacia
 * capas superiores (controllers / frontend).
 * </p>
 *
 * @param id identificador único del registro de predicción
 * @param aerolinea nombre o código de la aerolínea
 * @param origen código IATA del aeropuerto de origen
 * @param destino código IATA del aeropuerto de destino
 * @param fechaPartida fecha y hora estimada de partida del vuelo
 * @param prevision resultado de la predicción del modelo
 * @param probabilidad probabilidad asociada a la predicción
 * @param fechaPrediccion fecha y hora en que se realizó la predicción
 */
public record PredictionHistoryDTO(
        Long id,

        /** Aerolínea asociada al vuelo */
        String aerolinea,

        /** Código IATA del aeropuerto de origen */
        String origen,

        /** Código IATA del aeropuerto de destino */
        String destino,

        /** Fecha y hora programada de salida del vuelo */
        LocalDateTime fechaPartida,

        /** Estado estimado del vuelo según el modelo predictivo */
        String prevision,

        /** Nivel de confianza de la predicción (0.0 a 1.0) */
        Double probabilidad,

        /** Fecha y hora en que se generó la predicción */
        LocalDateTime fechaPrediccion
) {}
