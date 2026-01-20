package com.flightOnTime.flightOnTime.controller;

import com.flightOnTime.flightOnTime.dto.FlightRequestDTO;
import com.flightOnTime.flightOnTime.dto.PredictionResponseDTO;
import com.flightOnTime.flightOnTime.service.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/predict")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5500") //front
public class PredictionController {

    private final PredictionService predictionService;

    // ------------------------------
    // Endpoint POST /predict
    // ------------------------------
    @PostMapping
    public ResponseEntity<?> predict(@RequestBody FlightRequestDTO request) {
        try {
            PredictionResponseDTO response = predictionService.predict(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Manejo de errores simple
            return ResponseEntity.status(500).body(Map.of(
                    "error", "No se pudo obtener predicción",
                    "detalle", e.getMessage()
            ));
        }
    }

    // ------------------------------
    // Endpoint GET /predict/health
    // ------------------------------
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of(
                "status", "BACKEND OK"
        ));
    }
}