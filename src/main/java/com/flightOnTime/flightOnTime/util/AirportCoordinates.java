package com.flightOnTime.flightOnTime.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * Componente utilitario que provee coordenadas geográficas
 * (latitud y longitud) asociadas a códigos IATA de aeropuertos.
 *
 * <p>
 * Este componente se utiliza como fuente estática de referencia
 * para resolver la ubicación de aeropuertos sin depender de
 * servicios externos en tiempo de ejecución.
 * </p>
 *
 * <p>
 * Los datos incluidos corresponden a aeropuertos internacionales
 * de alto tráfico y relevancia global.
 * </p>
 *
 * <h3>Uso principal</h3>
 * <ul>
 *     <li>Obtención de coordenadas para consultas meteorológicas</li>
 *     <li>Normalización de datos a partir de códigos IATA</li>
 *     <li>Evitar llamadas innecesarias a APIs externas</li>
 * </ul>
 *
 * <h3>Consideraciones</h3>
 * <ul>
 *     <li>El mapa es inmutable y thread-safe</li>
 *     <li>Los códigos IATA se manejan de forma case-insensitive</li>
 *     <li>Si el aeropuerto no existe, se retorna {@link java.util.Optional#empty()}</li>
 * </ul>
 *
 * <p>
 * En una evolución futura, esta implementación puede ser
 * reemplazada por una base de datos o configuración externa
 * sin modificar la interfaz pública.
 * </p>
 */
@Component
public class AirportCoordinates {

    /**
     * Mapa inmutable que asocia códigos IATA de aeropuertos
     * con sus coordenadas geográficas.
     *
     * <p>
     * El arreglo de {@code double[]} sigue el orden:
     * <ul>
     *     <li>Índice 0 → latitud</li>
     *     <li>Índice 1 → longitud</li>
     * </ul>
     * </p>
     */
    private static final Map<String, double[]> AIRPORTS = Map.ofEntries(

            Map.entry("EZE", new double[]{-34.8222, -58.5358}),
            Map.entry("AEP", new double[]{-34.5592, -58.4156}),
            Map.entry("JFK", new double[]{40.6413, -73.7781}),
            Map.entry("LAX", new double[]{33.9416, -118.4085}),
            Map.entry("ORD", new double[]{41.9742, -87.9073}),
            Map.entry("ATL", new double[]{33.6407, -84.4277}),
            Map.entry("DFW", new double[]{32.8998, -97.0403}),
            Map.entry("DEN", new double[]{39.8561, -104.6737}),
            Map.entry("DXB", new double[]{25.2532, 55.3657}),
            Map.entry("LHR", new double[]{51.4700, -0.4543}),
            Map.entry("CDG", new double[]{49.0097, 2.5479}),
            Map.entry("AMS", new double[]{52.3105, 4.7683}),
            Map.entry("FRA", new double[]{50.0379, 8.5622}),
            Map.entry("MAD", new double[]{40.4983, -3.5676}),
            Map.entry("BCN", new double[]{41.2974, 2.0833}),
            Map.entry("IST", new double[]{41.2753, 28.7519}),
            Map.entry("HND", new double[]{35.5494, 139.7798}),
            Map.entry("NRT", new double[]{35.7720, 140.3929}),
            Map.entry("ICN", new double[]{37.4602, 126.4407}),
            Map.entry("SIN", new double[]{1.3644, 103.9915}),
            Map.entry("HKG", new double[]{22.3080, 113.9185}),
            Map.entry("PVG", new double[]{31.1443, 121.8083}),
            Map.entry("GRU", new double[]{-23.4356, -46.4731}),
            Map.entry("SCL", new double[]{-33.3929, -70.7858}),
            Map.entry("MEX", new double[]{19.4361, -99.0719}),
            Map.entry("LIM", new double[]{-12.0219, -77.1143}),
            Map.entry("BOG", new double[]{4.7016, -74.1469}),
            Map.entry("JNB", new double[]{-26.1337, 28.2420}),
            Map.entry("BOS", new double[]{42.3656, -71.0096}),  // Boston Logan International
            Map.entry("SFO", new double[]{37.6213, -122.3790}), // San Francisco International
            Map.entry("MDW", new double[]{41.7858, -87.7522}),  // Chicago Midway
            Map.entry("MIA", new double[]{25.7959, -80.2870}),  // Miami International
            Map.entry("EWR", new double[]{40.6895, -74.1745}),  // Newark Liberty
            Map.entry("OAK", new double[]{37.7126, -122.2197}), // Oakland
            Map.entry("LAS", new double[]{36.0840, -115.1537})  // Las Vegas McCarran
    );

    /**
     * Obtiene las coordenadas geográficas asociadas a un código IATA.
     *
     * @param iata código IATA del aeropuerto (ej: "EZE", "JFK")
     * @return {@link Optional} con un arreglo {@code double[]} donde:
     *         <ul>
     *             <li>posición 0 → latitud</li>
     *             <li>posición 1 → longitud</li>
     *         </ul>
     *         o {@link Optional#empty()} si el aeropuerto no está soportado
     */
    public Optional<double[]> getCoordinates(String iata) {
        return Optional.ofNullable(
                AIRPORTS.get(iata.toUpperCase())
        );
    }
}
