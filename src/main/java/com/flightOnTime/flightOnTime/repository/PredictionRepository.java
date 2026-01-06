package com.flightOnTime.flightOnTime.repository;

import com.flightOnTime.flightOnTime.entity.Prediction;
import com.flightOnTime.flightOnTime.enums.PredictionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    long countByPrevision(PredictionStatus prevision);

}
