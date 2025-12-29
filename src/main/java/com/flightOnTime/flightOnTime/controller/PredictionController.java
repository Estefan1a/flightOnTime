package com.flightOnTime.flightOnTime.controller;

import com.flightOnTime.flightOnTime.dto.PredictionRequestDTO;
import com.flightOnTime.flightOnTime.dto.PredictionResponseDTO;
import com.flightOnTime.flightOnTime.service.PredictionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/predict")
public class PredictionController {

    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping
    public PredictionResponseDTO predict(
            @Valid @RequestBody PredictionRequestDTO datos) {

        return predictionService.predict(datos);
    }

}
