package com.flightOnTime.flightOnTime.repository;

import com.flightOnTime.flightOnTime.dto.AirportStatsDTO;
import com.flightOnTime.flightOnTime.entity.Prediction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AirportStatsRepository
        extends JpaRepository<Prediction, Long> {

    /**
     * Obtiene un ranking de aeropuertos de origen basado en la cantidad de vuelos
     * con predicción de retraso.
     *
     * <p>
     * Para cada aeropuerto se calculan las siguientes métricas:
     * </p>
     * <ul>
     *     <li>Total de predicciones realizadas</li>
     *     <li>Total de vuelos con predicción {@code RETRASADO}</li>
     *     <li>Porcentaje de vuelos retrasados sobre el total del aeropuerto</li>
     * </ul>
     *
     * <p>
     * El ranking se ordena de forma descendente según la cantidad total
     * de vuelos retrasados, mostrando primero los aeropuertos con mayor
     * impacto operativo.
     * </p>
     *
     * <p>
     * Este método utiliza una proyección DTO ({@link AirportStatsDTO})
     * para evitar la carga de entidades completas y mejorar el rendimiento.
     * </p>
     *
     * @param pageable permite limitar la cantidad de resultados
     *                 (por ejemplo: top 5 aeropuertos)
     * @return lista ordenada de estadísticas por aeropuerto
     */
    @Query("""
    SELECT new com.flightOnTime.flightOnTime.dto.AirportStatsDTO(
        f.origen,
        COUNT(p),
        SUM(CASE WHEN p.prevision = com.flightOnTime.flightOnTime.enums.PredictionStatus.RETRASADO THEN 1 ELSE 0 END),
        (SUM(CASE WHEN p.prevision = com.flightOnTime.flightOnTime.enums.PredictionStatus.RETRASADO THEN 1 ELSE 0 END) * 1.0) / COUNT(p)
    )
    FROM Prediction p
    JOIN p.flightRequest f
    GROUP BY f.origen
    ORDER BY SUM(CASE WHEN p.prevision = RETRASADO THEN 1 ELSE 0 END) DESC
    """)
    List<AirportStatsDTO> getTopProblematicAirports(Pageable pageable);
}

