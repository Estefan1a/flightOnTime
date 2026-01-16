package com.flightOnTime.flightOnTime.dto;

import java.time.LocalDateTime;

public record PredictionHistoryDTO(
        Long id,
        String aerolinea,
        String origen,
        String destino,
        LocalDateTime fechaPartida,
        String prevision,
        Double probabilidad,
        LocalDateTime fechaPrediccion
) {}