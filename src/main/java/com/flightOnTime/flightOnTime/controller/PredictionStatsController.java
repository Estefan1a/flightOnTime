package com.flightOnTime.flightOnTime.controller;

import com.flightOnTime.flightOnTime.dto.PredictionStatsDTO;
import com.flightOnTime.flightOnTime.service.PredictionStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST encargado de exponer estadísticas generales
 * sobre las predicciones de vuelos.
 *
 * Proporciona métricas globales del sistema, como:
 * - Total de predicciones realizadas
 * - Cantidad de vuelos puntuales
 * - Cantidad de vuelos retrasados
 * - Cantidad de vuelos cancelados
 * - Porcentaje de vuelos retrasados
 *
 * Ruta base:
 *   /stats
 *
 * Uso típico:
 * - Mostrar métricas generales en dashboards o paneles administrativos
 */
@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@CrossOrigin("http://127.0.0.1:5500")
public class PredictionStatsController {

    private final PredictionStatsService statsService;

    /**
     * Obtiene las estadísticas globales de predicciones.
     *
     * Delegado al {@link PredictionStatsService}, que centraliza
     * el cálculo de métricas a partir de los datos persistidos.
     *
     * @return estadísticas generales del sistema de predicción (200 OK)
     */
    @GetMapping
    public ResponseEntity<PredictionStatsDTO> getStats() {
        return ResponseEntity.ok(
                statsService.getStats()
        );
    }
}
