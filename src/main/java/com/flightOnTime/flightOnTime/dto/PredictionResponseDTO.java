package com.flightOnTime.flightOnTime.dto;

public record PredictionResponseDTO(
        FlightStatus prediction,
        Double probability
) {
}
