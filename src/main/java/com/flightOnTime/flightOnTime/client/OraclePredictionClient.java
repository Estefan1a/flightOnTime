package com.flightOnTime.flightOnTime.client;

import com.flightOnTime.flightOnTime.dto.FlightRequestDTO;
import com.flightOnTime.flightOnTime.dto.PredictionResponseDTO;
import com.flightOnTime.flightOnTime.exception.OraclePredictionException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
/*
@Component
@AllArgsConstructor
public class OraclePredictionClient {
   private final RestTemplate restTemplate;

      @Value("${oracle.prediction.url}")
    private String oracleUrl;

   public PredictionResponseDTO predict(FlightRequestDTO request) {
        try {
            return restTemplate.postForObject(
                    oracleUrl,
                    request,
                    PredictionResponseDTO.class
            );
        } catch (RestClientException ex) {
            throw new OraclePredictionException("No se pudo obtener predicción del oráculo", ex);
        }
    }
}
*/

    @Component
    @Profile("mock-oracle")
    public class OraclePredictionClient {

        public PredictionResponseDTO predict(FlightRequestDTO request) {

            return new PredictionResponseDTO(
                    "RETRASADO",
                    0.85
            );
        }
    }



