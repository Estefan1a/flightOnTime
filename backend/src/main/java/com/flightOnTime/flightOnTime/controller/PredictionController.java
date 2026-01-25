package com.flightOnTime.flightOnTime.controller;

import com.flightOnTime.flightOnTime.dto.FlightRequestDTO;
import com.flightOnTime.flightOnTime.dto.PredictionHistoryDTO;
import com.flightOnTime.flightOnTime.dto.PredictionResponseDTO;
import com.flightOnTime.flightOnTime.service.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/predict")
@RequiredArgsConstructor
@CrossOrigin(origins = "${frontend.url}") //front
public class PredictionController {

    private final PredictionService predictionService;

    private static final Logger log =
            LoggerFactory.getLogger(PredictionController.class);


    // ------------------------------
    // Endpoint POST /predict
    // ------------------------------
    @PostMapping
    public ResponseEntity<?> predict(@RequestBody FlightRequestDTO request) {
        try {
            log.info("[BACKEND - Controller] Request recibido: aerolinea={}, origen={}, destino={}, fecha={}, distancia={}",
                    request.aerolinea(),
                    request.origen(),
                    request.destino(),
                    request.fechaPartida(),
                    request.distanciaKm()
            );

            PredictionResponseDTO response = predictionService.predict(request);

            log.info("[BACKEND - Controller] Respuesta enviada: prevision={}, probabilidad={}",
                    response.prevision(),
                    response.probabilidad()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {

            log.error("[BACKEND - Controller] Error al procesar predicción", e);
            return ResponseEntity.status(500).body(Map.of(
                    "error", "No se pudo obtener predicción",
                    "detalle", e.getMessage()
            ));
        }
    }

    // ------------------------------
    // History
    // ------------------------------
    @GetMapping("/history")
    public List<PredictionHistoryDTO> getHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return predictionService.getHistory(page, size);
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