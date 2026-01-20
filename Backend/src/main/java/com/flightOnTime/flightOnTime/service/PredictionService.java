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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PredictionService {

    private final FlightRequestRepository flightRepo;
    private final PredictionRepository predictionRepo;
    private final FlightMapper flightMapper;
    private final OraclePredictionClient oracleClient;
    private final WeatherService weatherService;

    // ------------------------------
    // Predicción de un vuelo
    // ------------------------------
    public PredictionResponseDTO predict(FlightRequestDTO request) {

        // 1️⃣ Persistimos el vuelo
        FlightRequest flight = flightRepo.save(flightMapper.toEntity(request));

        // 2️⃣ Obtenemos info de clima usando solo la fecha (sin hora)
        LocalDate fecha = request.fechaPartida().toLocalDate();
        WeatherInfo weather = weatherService.getWeatherForAirportAndDate(
                request.destino(),
                fecha
        );

        // 3️⃣ Consultamos el oráculo Flask
        Map<String, Object> oracleResult = oracleClient.predict(request);

        // 4️⃣ Guardamos predicción
        String estado = (String) oracleResult.get("prevision");
        Double probabilidad = ((Number) oracleResult.get("probabilidad")).doubleValue();

        // Mapear respuesta del microservicio a tu enum
        PredictionStatus statusEnum;
        switch (estado.toUpperCase()) {
            case "A TIEMPO":
            case "PUNTUAL":
                statusEnum = PredictionStatus.PUNTUAL;
                break;
            case "RETRASADO":
                statusEnum = PredictionStatus.RETRASADO;
                break;
            default:
                throw new RuntimeException("Valor desconocido de predicción: " + estado);
        }

        predictionRepo.save(
                Prediction.builder()
                        .flightRequest(flight)
                        .prevision(statusEnum)
                        .probabilidad(probabilidad)
                        .fechaPrediccion(LocalDateTime.now())
                        .build()
        );

        // 5️⃣ Devolvemos respuesta
        return new PredictionResponseDTO(
                estado,
                probabilidad,
                weather
        );
    }

    // ------------------------------
    // Historial de predicciones
    // ------------------------------
    public List<PredictionHistoryDTO> getHistory(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaPrediccion").descending());

        Page<Prediction> predictions = predictionRepo.findAll(pageable);

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
