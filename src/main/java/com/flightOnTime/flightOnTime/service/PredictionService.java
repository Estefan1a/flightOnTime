package com.flightOnTime.flightOnTime.service;

import com.flightOnTime.flightOnTime.client.OraclePredictionClient;
import com.flightOnTime.flightOnTime.dto.FlightRequestDTO;
import com.flightOnTime.flightOnTime.dto.PredictionResponseDTO;
import com.flightOnTime.flightOnTime.entity.FlightRequest;
import com.flightOnTime.flightOnTime.entity.Prediction;
import com.flightOnTime.flightOnTime.enums.PredictionStatus;
import com.flightOnTime.flightOnTime.mapper.FlightMapper;
import com.flightOnTime.flightOnTime.repository.FlightRequestRepository;
import com.flightOnTime.flightOnTime.repository.PredictionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PredictionService {

    private final FlightRequestRepository flightRepo;
    private final PredictionRepository predictionRepo;
    private final FlightMapper flightMapper;
    private final OraclePredictionClient oracleClient;

    public PredictionResponseDTO predict(FlightRequestDTO request) {

        FlightRequest flight = flightRepo.save(flightMapper.toEntity(request));

        PredictionResponseDTO oracleResponse = oracleClient.predict(request);

        predictionRepo.save(Prediction.builder()
                        .flightRequest(flight)
                        .prevision(PredictionStatus.valueOf(oracleResponse.prevision().toUpperCase()))
                        .probabilidad(oracleResponse.probabilidad())
                        .fechaPrediccion(LocalDateTime.now())
                        .build()
        );

        return oracleResponse;
    }
}
