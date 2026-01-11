package com.flightOnTime.flightOnTime.service;

import com.flightOnTime.flightOnTime.dto.AirportStatsDTO;
import com.flightOnTime.flightOnTime.repository.AirportStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de calcular estadísticas operativas
 * relacionadas con aeropuertos de origen.
 *
 * Expone métricas agregadas basadas en las predicciones
 * realizadas por el sistema, como:
 * - Total de predicciones
 * - Cantidad de vuelos retrasados
 * - Porcentaje de retrasos
 *
 * La lógica de ranking se delega al repositorio,
 * mientras que este servicio define reglas de negocio
 * como el tamaño del ranking (top 5).
 */
@Service
@RequiredArgsConstructor
public class AirportStatsService {

    private final AirportStatsRepository airportStatsRepository;

    /**
     * Obtiene el ranking de los 5 aeropuertos de origen
     * con mayor cantidad de vuelos retrasados.
     *
     * El resultado se encuentra ordenado de mayor a menor
     * impacto operativo.
     *
     * @return lista de estadísticas por aeropuerto
     */
    public List<AirportStatsDTO> getTop5Airports() {
        return airportStatsRepository.getTopProblematicAirports(
                PageRequest.of(0, 5)
        );
    }
}
