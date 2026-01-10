package com.flightOnTime.flightOnTime.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record FlightRequestDTO(
        @NotBlank(message = "La aerolínea es obligatoria")
        String aerolinea,

        @NotBlank(message = "El aeropuerto de origen es obligatorio")
        @Size(max = 5, message = "El código de origen no puede superar los 5 caracteres")
        String origen,

        @NotBlank(message = "El aeropuerto de destino es obligatorio")
        @Size(max = 5, message = "El código de destino no puede superar los 5 caracteres")
        String destino,

        @NotNull(message = "La fecha de partida es obligatoria")
        @Future(message = "La fecha de salida debe ser posterior a la fecha actual")
        LocalDateTime fechaPartida,

        @NotNull(message = "La distancia del vuelo es obligatoria")
        @Positive(message = "La distancia del vuelo debe ser mayor a cero")
        Integer distanciaKm
) {
}
