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

