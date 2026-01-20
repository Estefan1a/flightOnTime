package com.flightOnTime.flightOnTime.repository;

import com.flightOnTime.flightOnTime.entity.Prediction;
import com.flightOnTime.flightOnTime.enums.PredictionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    // Contar vuelos por estado
    long countByPrevision(PredictionStatus status);

    // Estadísticas de aeropuertos
    @Query("SELECT p.flightRequest.origen, COUNT(p), SUM(CASE WHEN p.prevision = 'RETRASADO' THEN 1 ELSE 0 END) " +
            "FROM Prediction p " +
            "GROUP BY p.flightRequest.origen " +
            "ORDER BY SUM(CASE WHEN p.prevision = 'RETRASADO' THEN 1 ELSE 0 END) DESC")
    List<Object[]> findAirportStats();
}