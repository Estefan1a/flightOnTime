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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@RequiredArgsConstructor
public class PredictionService {

    private final FlightRequestRepository flightRepo;
    private final PredictionRepository predictionRepo;
    private final FlightMapper flightMapper;
    private final OraclePredictionClient oracleClient;
    private final WeatherService weatherService;
    private static final Logger log =
            LoggerFactory.getLogger(PredictionService.class);


    // ------------------------------
    // Predicción de un vuelo
    // ------------------------------
    public PredictionResponseDTO predict(FlightRequestDTO request) {

        log.info("[SERVICE] Iniciando flujo de predicción para vuelo {} → {}",
                request.origen(), request.destino());

        // 1️⃣ Persistimos el vuelo
        FlightRequest flight = flightRepo.save(flightMapper.toEntity(request));
        log.info("[SERVICE] FlightRequest guardado con ID={}", flight.getId());

        // 2️⃣ Obtenemos info de clima usando solo la fecha (sin hora)
        LocalDate fecha = request.fechaPartida().toLocalDate();
        WeatherInfo weather = weatherService.getWeatherForAirportAndDate(
                request.destino(),
                fecha
        );

        log.info("[SERVICE] Clima obtenido: temp={}°C, viento={} km/h",
                weather.temperatura(),
                weather.viento()
        );

        // 3️⃣ Consultamos el oráculo Flask
        log.info("[SERVICE] Enviando datos al microservicio Flask");
        Map<String, Object> oracleResult = oracleClient.predict(request);

        String estado = (String) oracleResult.get("prevision");
        Double probabilidad = ((Number) oracleResult.get("probabilidad")).doubleValue();

        log.info("[SERVICE] Respuesta del microservicio: estado={}, probabilidad={}",
                estado, probabilidad
        );

        // 4️⃣ Guardamos predicción
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

        log.info("[SERVICE] Predicción guardada correctamente en base de datos");

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
