package com.flightOnTime.flightOnTime.controller;

import com.flightOnTime.flightOnTime.dto.FlightRequestDTO;
import com.flightOnTime.flightOnTime.dto.PredictionResponseDTO;
import com.flightOnTime.flightOnTime.service.PredictionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST encargado de exponer el endpoint de predicción de vuelos.
 *
 * Recibe la información de un vuelo (origen, destino, fecha, aerolínea, etc.)
 * y delega la lógica de predicción al {@link PredictionService}, el cual
 * se encarga de comunicarse con el oráculo de predicciones (modelo de Data Science).
 *
 * Ruta base:
 *   /predict
 *
 * Método soportado:
 * - POST: genera una predicción de estado del vuelo
 */
@RestController
@RequestMapping("/predict")
@RequiredArgsConstructor
public class PredictionController {

    private final PredictionService predictionService;

    /**
     * Genera una predicción para un vuelo a partir de los datos proporcionados.
     *
     * Valida automáticamente el cuerpo del request utilizando las anotaciones
     * de Bean Validation presentes en {@link FlightRequestDTO}.
     *
     * Flujo:
     * 1. Valida la entrada
     * 2. Ejecuta la predicción
     * 3. Devuelve el resultado en formato JSON
     *
     * @param flightRequestDTO datos del vuelo a evaluar
     * @return predicción del estado del vuelo (200 OK)
     */
    @PostMapping
    public ResponseEntity<PredictionResponseDTO> predict(
            @Valid @RequestBody FlightRequestDTO flightRequestDTO
    ) {
        return ResponseEntity.ok(
                predictionService.predict(flightRequestDTO)
        );
    }
}
