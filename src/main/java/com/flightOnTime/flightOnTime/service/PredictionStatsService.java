package com.flightOnTime.flightOnTime.service;

import com.flightOnTime.flightOnTime.dto.PredictionStatsDTO;
import com.flightOnTime.flightOnTime.enums.PredictionStatus;
import com.flightOnTime.flightOnTime.repository.PredictionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
/**
 * Servicio encargado de calcular estadísticas generales
 * sobre las predicciones realizadas por el sistema.
 *
 * Provee métricas agregadas como:
 * - Total de predicciones realizadas
 * - Total de vuelos puntuales
 * - Total de vuelos retrasados
 * - Porcentaje de vuelos retrasados
 *
 * Los datos se obtienen directamente desde la base de datos
 * utilizando consultas de agregación del repositorio.
 */
@Service
@RequiredArgsConstructor
public class PredictionStatsService {

    private final PredictionRepository predictionRepository;

    /**
     * Obtiene estadísticas globales de las predicciones.
     *
     * Calcula:
     * - cantidad total de predicciones
     * - cantidad de predicciones puntuales
     * - cantidad de predicciones retrasadas
     * - porcentaje de vuelos retrasados sobre el total
     *
     * En caso de no existir predicciones, el porcentaje de retrasos
     * se devuelve como 0 para evitar divisiones por cero.
     *
     * @return objeto {@link PredictionStatsDTO} con las métricas calculadas
     */
    public PredictionStatsDTO getStats() {

        long total = predictionRepository.count();

        long puntuales = predictionRepository
                .countByPrevision(PredictionStatus.PUNTUAL);

        long retrasados = predictionRepository
                .countByPrevision(PredictionStatus.RETRASADO);

        double porcentajeRetrasados =
                total == 0 ? 0.0 : (retrasados * 100.0) / total;

        return new PredictionStatsDTO(
                total,
                puntuales,
                retrasados,
                porcentajeRetrasados
        );
    }
}
