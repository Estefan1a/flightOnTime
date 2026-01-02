package com.flightOnTime.flightOnTime.service;

import com.flightOnTime.flightOnTime.dto.FlightStatus;
import com.flightOnTime.flightOnTime.dto.PredictionRequestDTO;
import com.flightOnTime.flightOnTime.dto.PredictionResponseDTO;
//import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
//@Primary
public class FakePredictionService  implements PredictionService  {
    @Override
    public PredictionResponseDTO predict(PredictionRequestDTO request) {
        return new PredictionResponseDTO(
                FlightStatus.PUNTUAL,
                0.43
        );
    }
}


