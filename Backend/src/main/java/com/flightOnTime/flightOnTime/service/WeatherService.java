package com.flightOnTime.flightOnTime.service;

import com.flightOnTime.flightOnTime.client.WeatherClient;
import com.flightOnTime.flightOnTime.dto.WeatherInfo;
import com.flightOnTime.flightOnTime.util.AirportCoordinates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
/**
 * Servicio de consulta climática para aeropuertos.
 *
 * <p>
 * Se encarga de obtener información meteorológica relevante
 * para un aeropuerto específico en una fecha determinada,
 * a partir de su código IATA.
 * </p>
 *
 * <p>
 * El servicio actúa como capa de orquestación entre:
 * </p>
 * <ul>
 *     <li>La resolución de coordenadas geográficas del aeropuerto</li>
 *     <li>La consulta a un proveedor externo de pronóstico del clima</li>
 * </ul>
 *
 * <p>
 * Actualmente devuelve:
 * </p>
 * <ul>
 *     <li>Temperatura máxima estimada</li>
 *     <li>Velocidad máxima del viento</li>
 * </ul>
 *
 * <p>
 * La información se utiliza como contexto adicional
 * para enriquecer la predicción de vuelos.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class WeatherService {

    /** Utilidad para resolver coordenadas geográficas a partir del código IATA */
    private final AirportCoordinates airportCoordinates;

    /** Cliente HTTP para consultar el servicio externo de clima */
    private final WeatherClient weatherClient;

    /**
     * Obtiene el pronóstico del clima para un aeropuerto
     * en la fecha del vuelo.
     *
     * <p>
     * El flujo es el siguiente:
     * </p>
     * <ol>
     *     <li>Convierte el código IATA en coordenadas geográficas</li>
     *     <li>Consulta el pronóstico diario para la fecha indicada</li>
     *     <li>Extrae temperatura máxima y velocidad máxima del viento</li>
     * </ol>
     *
     * @param iata código IATA del aeropuerto de destino
     * @param flightDate fecha y hora de salida del vuelo
     * @return información climática relevante para la predicción
     *
     * @throws IllegalArgumentException
     *         si el código IATA no está soportado
     * @throws IllegalStateException
     *         si la respuesta del proveedor de clima no es válida
     */
    public WeatherInfo getWeatherForAirportAndDate(
            String iata,
            LocalDateTime flightDate
    ) {

        double[] coords = airportCoordinates.getCoordinates(iata)
                .orElseThrow(() ->
                        new IllegalArgumentException("IATA no soportado: " + iata));

        Map<String, Object> response =
                weatherClient.getForecast(
                        coords[0],
                        coords[1],
                        LocalDate.from(flightDate)
                );

        Map<String, Object> daily =
                (Map<String, Object>) response.get("daily");

        List<Double> maxTemps =
                (List<Double>) daily.get("temperature_2m_max");

        List<Double> wind =
                (List<Double>) daily.get("windspeed_10m_max");

        double temperature = maxTemps.get(0);
        double windSpeed = wind.get(0);

        return new WeatherInfo(
                temperature,
                windSpeed
        );
    }
}
