package com.flightOnTime.flightOnTime.client;

import com.flightOnTime.flightOnTime.dto.FlightRequestDTO;
import com.flightOnTime.flightOnTime.dto.PredictionResponseDTO;
import com.flightOnTime.flightOnTime.exception.OraclePredictionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
/**
 * Test unitario del cliente {@link OraclePredictionClient}.
 *
 * Verifica el comportamiento del cliente encargado de comunicarse con
 * el servicio externo de predicción (Oracle), simulando respuestas mediante mocks.
 *
 * Se utiliza {@link MockitoExtension} para inyectar dependencias simuladas
 * y evitar llamadas reales a servicios externos.
 */@ExtendWith(MockitoExtension.class)
class OraclePredictionClientTest {

    /**
     * Cliente HTTP simulado utilizado para interceptar y controlar
     * las llamadas REST realizadas hacia el servicio Oracle.
     */
    @Mock
    private RestTemplate restTemplate;

    /**
     * Instancia del cliente Oracle bajo prueba.
     *
     * Se inyecta el {@link RestTemplate} mockeado para aislar
     * completamente el comportamiento del cliente.
     */
    @InjectMocks
    private OraclePredictionClient oracleClient;

    /**
     * Configuración previa a cada test.
     *
     * Se inyecta manualmente la URL del servicio Oracle mediante
     * {@link ReflectionTestUtils} para evitar depender de
     * configuraciones externas o archivos properties.
     */
    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(
                oracleClient,
                "oracleUrl",
                "http://fake-oracle/predict"
        );
    }

    /**
     * Verifica que el cliente Oracle devuelva correctamente
     * una predicción cuando el servicio externo responde exitosamente.
     *
     * Se simula una llamada REST exitosa que retorna un
     * {@link PredictionResponseDTO}, y se valida que el cliente
     * propague dicha respuesta sin modificaciones.
     */
    @Test
    void shouldReturnPredictionWhenOracleRespondsOk() {

        FlightRequestDTO request = new FlightRequestDTO(
                "AR",
                "EZE",
                "COR",
                LocalDateTime.now(),
                350
        );

        PredictionResponseDTO oracleResponse =
                new PredictionResponseDTO("RETRASADO", 0.85);

        when(restTemplate.postForObject(
                anyString(),
                any(),
                any(Class.class)
        )).thenReturn(oracleResponse);

        //ejecución
        PredictionResponseDTO result = oracleClient.predict(request);

        //validación
        assertThat(result.prevision()).isEqualTo("RETRASADO");
        assertThat(result.probabilidad()).isEqualTo(0.85);
    }

    /**
     * Verifica que el cliente Oracle lance una excepción de dominio
     * cuando el servicio externo falla.
     *
     * Se simula una excepción {@link RestClientException} durante la
     * llamada HTTP, representando un fallo del servicio Oracle.
     *
     * El cliente debe capturar dicha excepción técnica y transformarla
     * en una {@link OraclePredictionException}, desacoplando la capa
     * de dominio de detalles de infraestructura.
     */
    @Test
    void shouldThrowExceptionWhenOracleFails() {

        FlightRequestDTO request = new FlightRequestDTO(
                "AR",
                "EZE",
                "COR",
                LocalDateTime.now(),
                350
        );

        when(restTemplate.postForObject(
                anyString(),
                any(),
                any(Class.class)
        )).thenThrow(new RestClientException("Oracle down"));


        assertThatThrownBy(() -> oracleClient.predict(request))
                .isInstanceOf(OraclePredictionException.class)
                .hasMessageContaining("No se pudo obtener predicción");
    }
}
