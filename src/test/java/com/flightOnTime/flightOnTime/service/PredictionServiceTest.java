package com.flightOnTime.flightOnTime.service;

import com.flightOnTime.flightOnTime.client.OraclePredictionClient;
import com.flightOnTime.flightOnTime.dto.FlightRequestDTO;
import com.flightOnTime.flightOnTime.dto.PredictionResponseDTO;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias del servicio {@link PredictionService}.
 *
 * Este test valida el flujo principal de predicción de vuelos:
 * <ul>
 *   <li>Persistencia de la solicitud de vuelo</li>
 *   <li>Invocación al servicio externo de predicción (Oracle)</li>
 *   <li>Persistencia del resultado de la predicción</li>
 * </ul>
 *
 * Se utilizan mocks para aislar el comportamiento del servicio y
 * garantizar que las interacciones con repositorios y clientes externos
 * se realicen correctamente.
 */
@ExtendWith(MockitoExtension.class)
class PredictionServiceTest {
    /**
     * Repositorio de solicitudes de vuelo.
     * Simula la persistencia de la entidad {@link FlightRequest}.
     */
    @Mock
    private FlightRequestRepository flightRepo;

    /**
     * Repositorio de predicciones.
     * Simula el guardado de la entidad {@link Prediction}.
     */
    @Mock
    private PredictionRepository predictionRepo;

    /**
     * Mapper encargado de convertir el DTO de entrada
     * {@link FlightRequestDTO} en la entidad {@link FlightRequest}.
     */
    @Mock
    private FlightMapper flightMapper;

    /**
     * Cliente externo encargado de obtener la predicción
     * desde el sistema Oracle.
     */
    @Mock
    private OraclePredictionClient oracleClient;

    /**
     * Servicio bajo prueba.
     * Contiene la lógica principal de predicción de vuelos.
     */
    @InjectMocks
    private PredictionService predictionService;

    /**
     * Verifica que el servicio:
     * <ol>
     *   <li>Convierta el DTO de solicitud a entidad</li>
     *   <li>Guarde la solicitud de vuelo</li>
     *   <li>Consuma el cliente Oracle para obtener la predicción</li>
     *   <li>Guarde la predicción obtenida</li>
     *   <li>Devuelva correctamente el resultado al consumidor</li>
     * </ol>
     */
    @Test
    void shouldSaveFlightRequestCallOracleAndSavePrediction() {

        // ===== GIVEN =====
        // Se prepara una solicitud de vuelo válida que simula
        // los datos recibidos desde el controller

        FlightRequestDTO requestDTO = new FlightRequestDTO(
                "AR",
                "EZE",
                "COR",
                LocalDateTime.of(2026, 2, 5, 14, 30),
                350
        );

        // Se simula la entidad FlightRequest que será persistida
        // en la base de datos (con ID generado)

        FlightRequest flightEntity = new FlightRequest();
        flightEntity.setId(1L);

        // Se simula la respuesta del oráculo externo
        // indicando que el vuelo estará retrasado

        PredictionResponseDTO oracleResponse =
                new PredictionResponseDTO("RETRASADO", 0.85);

        // Se define el comportamiento de los mocks:
        // - El mapper convierte el DTO en entidad
        // - El repositorio guarda y devuelve la entidad
        // - El cliente del oráculo devuelve la predicción simulada
        // - El repositorio de predicciones guarda la predicción

        when(flightMapper.toEntity(requestDTO)).thenReturn(flightEntity);
        when(flightRepo.save(any(FlightRequest.class))).thenReturn(flightEntity);
        when(oracleClient.predict(requestDTO)).thenReturn(oracleResponse);
        when(predictionRepo.save(any(Prediction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ===== WHEN =====
        // Se ejecuta el método bajo prueba: el servicio procesa
        // la solicitud, llama al oráculo y persiste la información

        PredictionResponseDTO result =
                predictionService.predict(requestDTO);

        // ===== THEN =====
        // Se valida que la respuesta no sea nula y que los valores
        // retornados coincidan con la predicción del oráculo

        assertThat(result).isNotNull();
        assertThat(result.prevision()).isEqualTo("RETRASADO");
        assertThat(result.probabilidad()).isEqualTo(0.85);

        // Se verifica que:
        // - El vuelo fue guardado una vez
        // - El oráculo fue consultado una vez
        // - La predicción fue persistida una vez

        verify(flightRepo, times(1)).save(any(FlightRequest.class));
        verify(oracleClient, times(1)).predict(requestDTO);
        verify(predictionRepo, times(1)).save(any(Prediction.class));
    }

}