package com.flightOnTime.flightOnTime.service;

import com.flightOnTime.flightOnTime.client.OraclePredictionClient;
import com.flightOnTime.flightOnTime.dto.FlightRequestDTO;
import com.flightOnTime.flightOnTime.dto.PredictionHistoryDTO;
import com.flightOnTime.flightOnTime.dto.PredictionResponseDTO;
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
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio principal de predicción de vuelos.
 *
 * Orquesta el flujo completo de una predicción:
 * <ul>
 *     <li>Persiste la solicitud de vuelo recibida</li>
 *     <li>Consulta al oráculo de predicción (servicio externo o mock)</li>
 *     <li>Guarda el resultado de la predicción</li>
 *     <li>Devuelve la respuesta al cliente</li>
 * </ul>
 *
 * Este servicio actúa como punto central de integración
 * entre el backend y el motor de predicción.
 */
@Service
@RequiredArgsConstructor
public class PredictionService {

    private final FlightRequestRepository flightRepo;
    private final PredictionRepository predictionRepo;
    private final FlightMapper flightMapper;
    private final OraclePredictionClient oracleClient;

    /**
     * Ejecuta una predicción de vuelo a partir de los datos proporcionados.
     *
     * El flujo es el siguiente:
     * <ol>
     *     <li>Convierte el DTO en entidad y lo persiste</li>
     *     <li>Envía la solicitud al oráculo de predicción</li>
     *     <li>Persiste el resultado de la predicción</li>
     *     <li>Devuelve la respuesta del oráculo</li>
     * </ol>
     *
     * @param request datos del vuelo a evaluar
     * @return resultado de la predicción con estado y probabilidad
     *
     @throws OraclePredictionException propagada desde el cliente del oráculo
     */
    public PredictionResponseDTO predict(FlightRequestDTO request) {

        FlightRequest flight = flightRepo.save(
                flightMapper.toEntity(request)
        );

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

        return oracleResponse;
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
