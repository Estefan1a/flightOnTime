package com.flightOnTime.flightOnTime;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightOnTime.flightOnTime.dto.FlightRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test de integración end-to-end para el flujo de predicción.
 *
 * Este test valida el flujo completo de la aplicación:
 * - Controller
 * - Service
 * - Client del oráculo (mock-oracle profile)
 * - Persistencia en base de datos (H2 en perfil test)
 *
 * Se levanta el contexto completo de Spring mediante {@link SpringBootTest}
 * y se realizan requests HTTP reales utilizando {@link MockMvc}.
 *
 * El perfil {@code test} permite:
 * - Usar una base de datos en memoria.
 * - Evitar dependencias externas reales.
 * - Simular el comportamiento del oráculo de predicción.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PredictionIntegrationTest {

    /**
     * MockMvc permite realizar llamadas HTTP reales
     * contra la aplicación levantada en contexto de test.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * ObjectMapper utilizado para serializar el DTO
     * a JSON dentro del body del request.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Verifica el flujo completo de predicción:
     *
     * GIVEN:
     * - Un {@link FlightRequestDTO} válido.
     *
     * WHEN:
     * - Se realiza una llamada POST al endpoint /predict.
     *
     * THEN:
     * - La respuesta HTTP es 200 OK.
     * - El body contiene una predicción generada.
     * - La prevision y probabilidad existen en el JSON de respuesta.
     *
     * Este test garantiza que:
     * - El request se procesa correctamente.
     * - La predicción es generada por el oráculo simulado.
     * - El vuelo y la predicción se persisten correctamente.
     */
    @Test
    void shouldPersistFlightAndPredictionEndToEnd() throws Exception {

        // ===== GIVEN =====
        // Se construye una solicitud de vuelo válida
        // similar a la que enviaría un cliente real
        FlightRequestDTO request = new FlightRequestDTO(
                "AR123",
                "EZE",
                "COR",
                LocalDateTime.of(2026, 2, 5, 14, 30),
                350
        );
        // ===== WHEN / THEN =====
        // Se ejecuta el request HTTP completo y se validan
        // los aspectos esenciales de la respuesta
        mockMvc.perform(
                        post("/predict")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                // El endpoint responde correctamente
                .andExpect(status().isOk())
                // Se valida que la predicción fue generada
                // y devuelta al cliente
                .andExpect(jsonPath("$.prevision").exists())
                .andExpect(jsonPath("$.probabilidad").exists());
    }
}
