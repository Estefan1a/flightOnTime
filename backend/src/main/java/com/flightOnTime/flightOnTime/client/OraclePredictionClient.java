package com.flightOnTime.flightOnTime.client;

import com.flightOnTime.flightOnTime.dto.FlightRequestDTO;
import com.flightOnTime.flightOnTime.dto.PredictionResponseDTO;
import com.flightOnTime.flightOnTime.exception.OraclePredictionException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OraclePredictionClient {
    private final RestTemplate restTemplate;

    @Value("${oracle.prediction.url}")
    private String oracleUrl; // http://127.0.0.1:5000/predict

    public Map<String, Object> predict(FlightRequestDTO request) {

        try {
            // --- Headers ---
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // --- Convertir record a Map ---
            Map<String, Object> body = new HashMap<>();
            body.put("fechaPartida", request.fechaPartida().toString()); // LocalDateTime → String
            body.put("distanciaKm", request.distanciaKm());
            body.put("aerolinea", request.aerolinea());
            body.put("origen", request.origen());
            body.put("destino", request.destino());

//            // Agregar clima desde WeatherInfo
//            body.put("Temperature_Celsius", weather.getTemperature());
//            body.put("Wind_Speed_knots", weather.getWindSpeed());
//            body.put("Visibility_km", weather.getVisibility());

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            // --- Llamada POST a Flask ---
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    oracleUrl,
                    entity,
                    Map.class
            );

            return response.getBody();

        } catch (RestClientException ex) {
            throw new RuntimeException(
                    "No se pudo obtener predicción del oráculo Flask", ex
            );
        }
    }
}





