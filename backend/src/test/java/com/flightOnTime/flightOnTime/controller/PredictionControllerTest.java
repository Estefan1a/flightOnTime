package com.flightOnTime.flightOnTime.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightOnTime.flightOnTime.dto.FlightRequestDTO;
import com.flightOnTime.flightOnTime.dto.PredictionResponseDTO;
import com.flightOnTime.flightOnTime.dto.WeatherInfo;
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

@WebMvcTest(PredictionController.class)
class PredictionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PredictionService predictionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnPredictionSuccessfully() throws Exception {
        FlightRequestDTO request = new FlightRequestDTO(
                "DL", "JFK", "LAX", LocalDateTime.of(2026, 2, 5, 14, 30), 2500
        );

        PredictionResponseDTO response = new PredictionResponseDTO(
                "PUNTUAL",
                0.95,
                new WeatherInfo(22.0, 5.0)
        );

        when(predictionService.predict(any())).thenReturn(response);

        mockMvc.perform(
                        post("/predict")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prevision").value("PUNTUAL"))
                .andExpect(jsonPath("$.probabilidad").value(0.95))
                .andExpect(jsonPath("$.weather.temperatura").value(22.0))
                .andExpect(jsonPath("$.weather.viento").value(5.0));
    }
}
