package com.flightOnTime.flightOnTime.service;

import com.flightOnTime.flightOnTime.dto.PredictionRequestDTO;
import com.flightOnTime.flightOnTime.dto.PredictionResponseDTO;

public interface PredictionService {
    PredictionResponseDTO predict(PredictionRequestDTO request);
}
