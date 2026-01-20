package com.flightOnTime.flightOnTime.dto;

/**
 * DTO que representa la respuesta de una predicción de vuelo.
 *
 * <p>
 * Contiene el resultado calculado por el modelo predictivo junto con
 * información climática relevante en el aeropuerto de destino.
 * </p>
 *
 * <p>
 * Usado en:
 * <ul>
 *     <li>Respuesta del endpoint {@code POST /predict}</li>
 * </ul>
 * </p>
 *
 * Convenciones:
 * <ul>
 *     <li>{@code prevision}: estado estimado del vuelo (ej: "PUNTUAL", "RETRASADO")</li>
 *     <li>{@code probabilidad}: valor entre 0 y 1 que indica el nivel de confianza del modelo</li>
 *     <li>{@code weather}: información climática asociada al vuelo</li>
 * </ul>
 */
public record PredictionResponseDTO(

        /**
         * Estado estimado del vuelo según el modelo predictivo.
         */
        String prevision,

        /**
         * Probabilidad asociada a la predicción (0.0 a 1.0).
         */
        Double probabilidad,

        /**
         * Información climática del aeropuerto de destino
         * correspondiente a la fecha del vuelo.
         */
        WeatherInfo weather
) {}

