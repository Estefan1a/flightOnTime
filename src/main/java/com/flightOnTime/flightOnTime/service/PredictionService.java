package com.flightOnTime.flightOnTime.service;

import com.flightOnTime.flightOnTime.client.OraclePredictionClient;
import com.flightOnTime.flightOnTime.dto.FlightRequestDTO;
import com.flightOnTime.flightOnTime.dto.PredictionHistoryDTO;
import com.flightOnTime.flightOnTime.dto.PredictionResponseDTO;
import com.flightOnTime.flightOnTime.dto.WeatherInfo;
import com.flightOnTime.flightOnTime.entity.FlightRequest;
import com.flightOnTime.flightOnTime.entity.Prediction;
import com.flightOnTime.flightOnTime.enums.PredictionStatus;
import com.flightOnTime.flightOnTime.mapper.FlightMapper;
import com.flightOnTime.flightOnTime.repository.FlightRequestRepository;
import com.flightOnTime.flightOnTime.repository.PredictionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio principal de predicción de vuelos.
 *
 * <p>
 * Orquesta el flujo completo de una predicción:
 * </p>
 * <ul>
 *     <li>Persiste la solicitud de vuelo recibida</li>
 *     <li>Consulta el clima en el aeropuerto de destino para la fecha del vuelo</li>
 *     <li>Consulta al oráculo de predicción (servicio externo o mock)</li>
 *     <li>Guarda el resultado de la predicción</li>
 *     <li>Devuelve la respuesta al cliente enriquecida con información climática</li>
 * </ul>
 *
 * <p>
 * Este servicio actúa como punto central de integración entre:
 * </p>
 * <ul>
 *     <li>Persistencia de solicitudes y predicciones</li>
 *     <li>Motor de predicción (oráculo)</li>
 *     <li>Servicio externo de clima</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class PredictionService {

    /** Repositorio de solicitudes de vuelo */
    private final FlightRequestRepository flightRepo;

    /** Repositorio de predicciones */
    private final PredictionRepository predictionRepo;

    /** Mapper DTO ↔ entidad para solicitudes de vuelo */
    private final FlightMapper flightMapper;

    /** Cliente del servicio de predicción (oráculo real o mock) */
    private final OraclePredictionClient oracleClient;

    /** Servicio de consulta climática */
    private final WeatherService weatherService;
    /**
     * Ejecuta una predicción de vuelo a partir de los datos proporcionados.
     *
     * <p>
     * El flujo de ejecución es el siguiente:
     * </p>
     * <ol>
     *     <li>Convierte el {@link FlightRequestDTO} en entidad y lo persiste</li>
     *     <li>Obtiene la información climática del aeropuerto de destino
     *         para la fecha de partida</li>
     *     <li>Consulta al oráculo de predicción</li>
     *     <li>Persiste el resultado de la predicción</li>
     *     <li>Devuelve la respuesta combinando predicción y clima</li>
     * </ol>
     *
     * @param request datos del vuelo a evaluar
     * @return respuesta de predicción con estado, probabilidad y clima asociado
     *
     * @throws OraclePredictionException
     *         si el oráculo de predicción no responde o devuelve un error
     * @throws IllegalArgumentException
     *         si el aeropuerto de destino no tiene coordenadas soportadas
     */
    public PredictionResponseDTO predict(FlightRequestDTO request) {

        FlightRequest flight = flightRepo.save(
                flightMapper.toEntity(request)
        );
        WeatherInfo weather =
                weatherService.getWeatherForAirportAndDate(request.destino(), request.fechaPartida());
        PredictionResponseDTO oracleResponse =
                oracleClient.predict(request);

        predictionRepo.save(
                Prediction.builder()
                        .flightRequest(flight)
                        .prevision(
                                PredictionStatus.valueOf(
                                        oracleResponse.prevision().toUpperCase()
                                )
                        )
                        .probabilidad(oracleResponse.probabilidad())
                        .fechaPrediccion(LocalDateTime.now())
                        .build()
        );

        return new PredictionResponseDTO(
                oracleResponse.prevision(),
                oracleResponse.probabilidad(),
                weather
        );
    }

    /**
     * Obtiene el historial de predicciones paginado y ordenado
     * por fecha de predicción descendente.
     *
     * @param page número de página (base 0)
     * @param size tamaño de página
     * @return lista de predicciones históricas
     */
    public List<PredictionHistoryDTO> getHistory(int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("fechaPrediccion").descending()
        );

        Page<Prediction> predictions =
                predictionRepo.findAll(pageable);

        return predictions.getContent().stream()
                .map(p -> new PredictionHistoryDTO(
                        p.getId(),
                        p.getFlightRequest().getAerolinea(),
                        p.getFlightRequest().getOrigen(),
                        p.getFlightRequest().getDestino(),
                        p.getFlightRequest().getFechaPartida(),
                        p.getPrevision().name(),
                        p.getProbabilidad(),
                        p.getFechaPrediccion()
                ))
                .toList();
    }
}
