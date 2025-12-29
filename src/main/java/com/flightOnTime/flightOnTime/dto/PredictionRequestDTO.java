package com.flightOnTime.flightOnTime.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record PredictionRequestDTO(

        @NotBlank(message = "La aerolínea es obligatoria")
        String aerolinea,

        @NotBlank(message = "El origen es obligatorio")
        String origen,

        @NotBlank(message = "El destino es obligatorio")
        String destino,

        @NotNull(message = "La fecha de partida es obligatoria")
        LocalDateTime fechaPartida,

        @NotNull(message = "La distancia es obligatoria")
        @Positive(message = "La distancia debe ser mayor a 0")
        Integer distanciaKm) {

}
