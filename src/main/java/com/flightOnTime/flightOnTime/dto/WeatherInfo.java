package com.flightOnTime.flightOnTime.dto;

/**
 * DTO que representa información climática relevante para un vuelo.
 *
 * <p>
 * Se utiliza como parte de la respuesta de la predicción para
 * aportar contexto meteorológico en el aeropuerto de destino.
 * </p>
 *
 * <p>
 * Usado en:
 * <ul>
 *     <li>{@link PredictionResponseDTO}</li>
 * </ul>
 * </p>
 *
 * Convenciones:
 * <ul>
 *     <li>{@code temperatura}: temperatura estimada en grados Celsius</li>
 *     <li>{@code viento}: velocidad del viento en km/h</li>
 * </ul>
 */
public record WeatherInfo(

        /**
         * Temperatura estimada en el aeropuerto de destino (°C).
         */
        double temperatura,

        /**
         * Velocidad del viento estimada (km/h).
         */
        double viento
) {}
