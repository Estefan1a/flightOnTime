package com.flightOnTime.flightOnTime.service;

import com.flightOnTime.flightOnTime.dto.FlightStatus;
import com.flightOnTime.flightOnTime.dto.PredictionRequestDTO;
import com.flightOnTime.flightOnTime.dto.PredictionResponseDTO;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class PredictionService {

    public PredictionResponseDTO predict(PredictionRequestDTO request) {

        double probability = calculateProbability(request);
        FlightStatus status = probability >= 0.6
                ? FlightStatus.RETRASADO
                : FlightStatus.PUNTUAL;

        return new PredictionResponseDTO(status, probability);
    }

    private double calculateProbability(PredictionRequestDTO request) {

        double probability = 0.2; // base

        // Distancia larga
        if (request.distanciaKm() > 800) {
            probability += 0.3;
        }

        // Horario pico
        LocalTime time = request.fechaPartida().toLocalTime();
        if (time.isAfter(LocalTime.of(17, 0)) &&
                time.isBefore(LocalTime.of(21, 0))) {
            probability += 0.3;
        }

        // Limitar entre 0 y 1
        return Math.min(probability, 1.0);
    }
}

