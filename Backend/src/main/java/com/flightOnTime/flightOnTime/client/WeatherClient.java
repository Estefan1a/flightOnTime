package com.flightOnTime.flightOnTime.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Map;
/**
 * Cliente encargado de consultar el pronóstico climático para una ubicación
 * geográfica y una fecha determinada.
 *
 * <p>
 * Construye una solicitud HTTP hacia un proveedor externo de clima utilizando
 * coordenadas (latitud y longitud) y una fecha específica, devolviendo la
 * respuesta cruda del servicio.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class WeatherClient {

    /**
     * Cliente REST utilizado para realizar la llamada HTTP.
     */
    private final RestTemplate restTemplate;

    /**
     * URL base del servicio de clima.
     */
    @Value("${weather.api.url}")
    private String apiUrl;

    /**
     * Obtiene el pronóstico climático diario para una ubicación y fecha dadas.
     *
     * @param lat  latitud del aeropuerto
     * @param lon  longitud del aeropuerto
     * @param date fecha para la cual se solicita el pronóstico
     * @return mapa con la respuesta del servicio de clima
     */
    public Map<String, Object> getForecast(
            double lat,
            double lon,
            LocalDate date
    ) {

        String url = UriComponentsBuilder
                .fromHttpUrl(apiUrl)
                .queryParam("latitude", lat)
                .queryParam("longitude", lon)
                .queryParam(
                        "daily",
                        "temperature_2m_max,temperature_2m_min,windspeed_10m_max"
                )
                .queryParam("start_date", date)
                .queryParam("end_date", date)
                .queryParam("timezone", "auto")
                .toUriString();

        return restTemplate.getForObject(url, Map.class);
    }
}
