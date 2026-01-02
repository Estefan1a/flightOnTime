package com.flightOnTime.flightOnTime.service;
import com.flightOnTime.flightOnTime.dto.FlightStatus;
import com.flightOnTime.flightOnTime.dto.PredictionRequestDTO;
import com.flightOnTime.flightOnTime.dto.PredictionResponseDTO;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class ModelPredictionService implements PredictionService {
    //Integrar el modelo .joblib
//    private final DataScienceClient client;
//
//    public ModelPredictionService(DataScienceClient client) {
//        this.client = client;
//    }
//
//    @Override
//    public PredictionResponseDTO predict(PredictionRequestDTO request) {
//
//        PredictionResponseDTO modelResponse = client.predict(request);
//
//        return new PredictionResponseDTO(
//                FlightStatus.valueOf(String.valueOf(modelResponse.prediction())),
//                modelResponse.probability()
//        );
//    }

    public PredictionResponseDTO predict(PredictionRequestDTO request) {
        // Respuesta simulada
        return new PredictionResponseDTO(
                FlightStatus.RETRASADO,
                0.75
        );
    }

}

