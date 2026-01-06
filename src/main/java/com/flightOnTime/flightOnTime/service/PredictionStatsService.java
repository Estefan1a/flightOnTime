package com.flightOnTime.flightOnTime.service;

import com.flightOnTime.flightOnTime.dto.PredictionStatsDTO;
import com.flightOnTime.flightOnTime.enums.PredictionStatus;
import com.flightOnTime.flightOnTime.repository.PredictionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PredictionStatsService {

    private final PredictionRepository predictionRepository;

    public PredictionStatsDTO getStats() {

        long total = predictionRepository.count();

        long puntuales = predictionRepository
                .countByPrevision(PredictionStatus.PUNTUAL);

        long retrasados = predictionRepository
                .countByPrevision(PredictionStatus.RETRASADO);

        long cancelados = predictionRepository
                .countByPrevision(PredictionStatus.CANCELADO);

        double porcentajeRetrasados = total == 0 ? 0.0 : (retrasados * 100.0) / total;

        return new PredictionStatsDTO(
                total,
                puntuales,
                retrasados,
                cancelados,
                porcentajeRetrasados
        );
    }
}
