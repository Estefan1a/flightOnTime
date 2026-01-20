package com.flightOnTime.flightOnTime.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

public record FlightRequestDTO(

        /** Nombre o código de la aerolínea */
        @NotBlank(message = "La aerolínea es obligatoria")
        String aerolinea,

        /** Código IATA del aeropuerto de origen */
        @NotBlank(message = "El aeropuerto de origen es obligatorio")
        @Size(max = 3, message = "El código de origen no puede superar los 5 caracteres")
        String origen,

        /** Código IATA del aeropuerto de destino */
        @NotBlank(message = "El aeropuerto de destino es obligatorio")
        @Size(max = 3, message = "El código de destino no puede superar los 5 caracteres")
        String destino,

        /** Fecha y hora programada de salida del vuelo */
        @NotNull(message = "La fecha de partida es obligatoria")
        @Future(message = "La fecha de salida debe ser posterior a la fecha actual")
        LocalDateTime fechaPartida,

        /** Distancia estimada del vuelo en kilómetros */
        @NotNull(message = "La distancia del vuelo es obligatoria")
        @Positive(message = "La distancia del vuelo debe ser mayor a cero")
        Integer distanciaKm
) {}

