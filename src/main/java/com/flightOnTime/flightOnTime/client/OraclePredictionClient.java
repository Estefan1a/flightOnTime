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
/**
 * Cliente encargado de comunicarse con el microservicio de predicción (oráculo).
 *
 * Esta implementación realiza una llamada HTTP POST utilizando {@link RestTemplate}
 * hacia un servicio externo que contiene el modelo de predicción (por ejemplo,
 * un microservicio en Python con FastAPI).
 *
 * La URL del oráculo se obtiene desde configuración mediante la propiedad:
 * {@code oracle.prediction.url}.
 *
 * En caso de que el servicio externo falle o no esté disponible,
 * se encapsula el error en una {@link OraclePredictionException} para
 * evitar propagar errores técnicos hacia capas superiores.
 */
/**
@Component
@AllArgsConstructor
public class OraclePredictionClient {

    /**
     * Cliente HTTP utilizado para realizar la llamada al oráculo.

    private final RestTemplate restTemplate;

    /**
     * URL del microservicio de predicción.
     * Se inyecta desde el archivo de configuración.

    @Value("${oracle.prediction.url}")
    private String oracleUrl;

    /**
     * Envía la solicitud de predicción al oráculo externo.
     *
     * @param request datos del vuelo a evaluar
     * @return resultado de la predicción (estado y probabilidad)
     * @throws OraclePredictionException si el oráculo no responde o ocurre un error de red

    public PredictionResponseDTO predict(FlightRequestDTO request) {
        try {
            return restTemplate.postForObject(
                    oracleUrl,
                    request,
                    PredictionResponseDTO.class
            );
        } catch (RestClientException ex) {
            throw new OraclePredictionException(
                    "No se pudo obtener predicción del oráculo",
                    ex
            );
        }
    }
}
**/
/**
 * Implementación simulada del cliente de predicción.
 *
 * Se utiliza únicamente cuando la aplicación se ejecuta con el perfil
 * {@code mock-oracle}.
 *
 * Esta versión no realiza llamadas HTTP externas y devuelve siempre
 * una predicción fija, lo que permite:
 *
 * - Desarrollar el backend sin depender del microservicio de Data Science
 * - Ejecutar demos y pruebas locales
 * - Evitar fallos cuando el oráculo real no está disponible
 *
 * Ideal para entornos de desarrollo y hackathon.
 */
@Component
@Profile("mock-oracle")
public class OraclePredictionClient {

    /**
     * Devuelve una predicción simulada sin llamar a un servicio externo.
     *
     * @param request datos del vuelo (no se procesan realmente)
     * @return predicción mockeada
     */
    public PredictionResponseDTO predict(FlightRequestDTO request) {
        return new PredictionResponseDTO(
                "RETRASADO",
                0.85,
                null
        );
    }
}



