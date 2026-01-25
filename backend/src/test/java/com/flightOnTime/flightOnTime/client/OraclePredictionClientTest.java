package com.flightOnTime.flightOnTime.client;

import com.flightOnTime.flightOnTime.dto.FlightRequestDTO;
import com.flightOnTime.flightOnTime.dto.WeatherInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OraclePredictionClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OraclePredictionClient oracleClient;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(oracleClient, "oracleUrl", "http://fake-oracle/predict");
    }

    @Test
    void shouldReturnPredictionWhenOracleRespondsOk() {
        FlightRequestDTO request = new FlightRequestDTO("DL", "JFK", "LAX", LocalDateTime.now(), 2500);

        Map<String, Object> oracleResponse = new HashMap<>();
        oracleResponse.put("prevision", "PUNTUAL");
        oracleResponse.put("probabilidad", 0.95);
        oracleResponse.put("weather", new WeatherInfo(22.0, 5.0));

        // Mock correcto para postForEntity
        when(restTemplate.postForEntity(anyString(), any(), any(Class.class)))
                .thenReturn(new ResponseEntity<>(oracleResponse, HttpStatus.OK));

        Map<String, Object> result = oracleClient.predict(request);

        assertThat(result.get("prevision")).isEqualTo("PUNTUAL");
        assertThat(result.get("probabilidad")).isEqualTo(0.95);
        WeatherInfo weather = (WeatherInfo) result.get("weather");
        assertThat(weather.temperatura()).isEqualTo(22.0);
        assertThat(weather.viento()).isEqualTo(5.0);
    }

    @Test
    void shouldThrowExceptionWhenOracleFails() {
        FlightRequestDTO request = new FlightRequestDTO("DL", "JFK", "LAX", LocalDateTime.now(), 2500);

        when(restTemplate.postForEntity(anyString(), any(), any(Class.class)))
                .thenThrow(new RestClientException("Oracle down"));

        assertThatThrownBy(() -> oracleClient.predict(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No se pudo obtener predicción del oráculo Flask");
    }
}
