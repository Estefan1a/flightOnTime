package com.flightOnTime.flightOnTime.controller;

import com.flightOnTime.flightOnTime.dto.AirportStatsDTO;
import com.flightOnTime.flightOnTime.service.AirportStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para la obtención de estadísticas de aeropuertos.
 *
 * Expone endpoints relacionados con métricas operativas calculadas
 * a partir de las predicciones de vuelos realizadas por el sistema.
 *
 * Actualmente permite:
 * - Obtener un ranking de los aeropuertos más problemáticos
 *   según la cantidad de vuelos retrasados.
 *
 * Ruta base:
 *   /stats/airports
 *
 * CORS:
 * - Habilitado para el frontend local en http://127.0.0.1:5500
 */
@RestController
@RequestMapping("/stats/airports")
@CrossOrigin("http://127.0.0.1:5500")
@RequiredArgsConstructor
public class AirportStatsController {

    private final AirportStatsService airportStatsService;

    /**
     * Obtiene el ranking de los aeropuertos con mayor cantidad de vuelos retrasados.
     *
     * El resultado corresponde al top 5 de aeropuertos de origen,
     * ordenados de forma descendente según la cantidad de predicciones
     * con estado {@code RETRASADO}.
     *
     * @return lista de estadísticas por aeropuerto
     *         (200 OK)
     */
    @GetMapping("/top")
    public ResponseEntity<List<AirportStatsDTO>> getTopAirports() {
        return ResponseEntity.ok(
                airportStatsService.getTop5Airports()
        );
    }
}
