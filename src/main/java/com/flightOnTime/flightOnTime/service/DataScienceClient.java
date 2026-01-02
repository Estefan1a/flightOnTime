package com.flightOnTime.flightOnTime.service;

import com.flightOnTime.flightOnTime.dto.PredictionRequestDTO;
import com.flightOnTime.flightOnTime.dto.PredictionResponseDTO;
import org.springframework.web.client.RestTemplate;

public class DataScienceClient {
    private final RestTemplate restTemplate = new RestTemplate();

    private final String MODEL_URL = "http://localhost:8000/predict";

    public PredictionResponseDTO predict(PredictionRequestDTO request) {
        return restTemplate.postForObject(
                MODEL_URL,
                request,
                PredictionResponseDTO.class
        );
    }
}
