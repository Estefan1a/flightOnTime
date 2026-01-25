package com.flightOnTime.flightOnTime.service;

import com.flightOnTime.flightOnTime.client.OraclePredictionClient;
import com.flightOnTime.flightOnTime.dto.FlightRequestDTO;
import com.flightOnTime.flightOnTime.dto.PredictionResponseDTO;
import com.flightOnTime.flightOnTime.dto.WeatherInfo;
import com.flightOnTime.flightOnTime.entity.FlightRequest;
import com.flightOnTime.flightOnTime.entity.Prediction;
import com.flightOnTime.flightOnTime.mapper.FlightMapper;
import com.flightOnTime.flightOnTime.repository.FlightRequestRepository;
import com.flightOnTime.flightOnTime.repository.PredictionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalDate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PredictionServiceTest {

    @Mock
    private FlightRequestRepository flightRepo;

    @Mock
    private PredictionRepository predictionRepo;

    @Mock
    private FlightMapper flightMapper;

    @Mock
    private OraclePredictionClient oracleClient;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private PredictionService predictionService;

    @Test
    void shouldSaveFlightRequestCallOracleAndSavePrediction() {
        FlightRequestDTO request = new FlightRequestDTO(
                "DL", "JFK", "LAX", LocalDateTime.of(2026, 2, 5, 14, 30), 2500
        );

        FlightRequest flightEntity = new FlightRequest();
        flightEntity.setId(1L);

        WeatherInfo weather = new WeatherInfo(22.0, 5.0);

        Map<String, Object> oracleResult = Map.of(
                "prevision", "PUNTUAL",
                "probabilidad", 0.95
        );

        when(flightMapper.toEntity(request)).thenReturn(flightEntity);
        when(flightRepo.save(any(FlightRequest.class))).thenReturn(flightEntity);
        when(weatherService.getWeatherForAirportAndDate(any(), any())).thenReturn(weather);
        when(oracleClient.predict(request)).thenReturn(oracleResult);
        when(predictionRepo.save(any(Prediction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PredictionResponseDTO result = predictionService.predict(request);

        assertThat(result).isNotNull();
        assertThat(result.prevision()).isEqualTo("PUNTUAL");
        assertThat(result.probabilidad()).isEqualTo(0.95);
        assertThat(result.weather()).isEqualTo(weather);

        verify(flightRepo, times(1)).save(any(FlightRequest.class));
        verify(weatherService, times(1)).getWeatherForAirportAndDate(any(), any());
        verify(oracleClient, times(1)).predict(request);
        verify(predictionRepo, times(1)).save(any(Prediction.class));
    }
}
