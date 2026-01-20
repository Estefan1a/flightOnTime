package com.flightOnTime.flightOnTime.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightOnTime.flightOnTime.dto.FlightRequestDTO;
import com.flightOnTime.flightOnTime.dto.PredictionResponseDTO;
import com.flightOnTime.flightOnTime.service.PredictionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Test de la capa Controller para {@link PredictionController}.
 *
 * Este test valida el comportamiento del endpoint REST /predict,
 * asegurando que:
 * - El request HTTP es correctamente recibido y deserializado.
 * - La validación (@Valid) funciona con datos correctos.
 * - El controller delega la lógica de negocio al {@link PredictionService}.
 * - La respuesta HTTP es 200 OK.
 * - El body de la respuesta contiene la predicción esperada.
 *
 * Se utiliza {@link WebMvcTest} para cargar únicamente el contexto web,
 * sin levantar el contexto completo de la aplicación.
 *
 * El {@link PredictionService} se mockea para aislar el test de:
 * - La lógica de negocio.
 * - El acceso a base de datos.
 * - La llamada al oráculo (modelo externo).
 */
@WebMvcTest(PredictionController.class)
class PredictionControllerTest {

    /**
     * MockMvc permite simular requests HTTP al controller
     * sin necesidad de levantar un servidor real.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Servicio mockeado para controlar la respuesta
     * que devuelve la capa de negocio.
     */
    @MockitoBean
    private PredictionService predictionService;

    /**
     * ObjectMapper utilizado para serializar el DTO
     * a JSON en el body del request.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Verifica que el endpoint POST /predict:
     *
     * GIVEN:
     * - Un {@link FlightRequestDTO} válido.
     * - Una respuesta mockeada del {@link PredictionService}.
     *
     * WHEN:
     * - Se realiza una llamada POST al endpoint /predict
     *   con un body JSON válido.
     *
     * THEN:
     * - La respuesta HTTP es 200 OK.
     * - El JSON de respuesta contiene los campos esperados:
     *   - prevision
     *   - probabilidad
     */
    @Test
    void shouldReturnPredictionSuccessfully() throws Exception {

        // ===== GIVEN =====
        FlightRequestDTO request = new FlightRequestDTO(
                "AR123",
                "EZE",
                "COR",
                LocalDateTime.of(2026, 2, 5, 14, 30),
                350
        );

        PredictionResponseDTO response = new PredictionResponseDTO(
                "RETRASADO",
                0.85
        );

        when(predictionService.predict(any()))
                .thenReturn(response);

        // ===== WHEN / THEN =====
        mockMvc.perform(
                        post("/predict")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prevision").value("RETRASADO"))
                .andExpect(jsonPath("$.probabilidad").value(0.85));
    }
}
