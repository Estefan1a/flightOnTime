package com.flightOnTime.flightOnTime.service;

import com.flightOnTime.flightOnTime.client.WeatherClient;
import com.flightOnTime.flightOnTime.dto.WeatherInfo;
import com.flightOnTime.flightOnTime.util.AirportCoordinates;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final AirportCoordinates airportCoordinates;
    private final WeatherClient weatherClient;

    public WeatherInfo getWeatherForAirportAndDate(String iata, LocalDate date) {

        double[] coords = airportCoordinates.getCoordinates(iata)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Aeropuerto no soportado: " + iata
                ));

        Map<String, Object> response = weatherClient.getForecast(coords[0], coords[1], date);

        // Extraemos los datos que nos interesan
        Map<String, Object> daily = (Map<String, Object>) response.get("daily");
        double temp = ((Number) ((java.util.List<?>) daily.get("temperature_2m_max")).get(0)).doubleValue();
        double wind = ((Number) ((java.util.List<?>) daily.get("windspeed_10m_max")).get(0)).doubleValue();

        return new WeatherInfo(temp, wind);
    }
}

