package com.flightOnTime.flightOnTime.mapper;

import com.flightOnTime.flightOnTime.dto.FlightRequestDTO;
import com.flightOnTime.flightOnTime.entity.FlightRequest;
import org.springframework.stereotype.Component;

@Component
public class FlightMapper {

    public FlightRequest toEntity(FlightRequestDTO dto) {
        return FlightRequest.builder()
                .aerolinea(dto.aerolinea())
                .origen(dto.origen())
                .destino(dto.destino())
                .fechaPartida(dto.fechaPartida())
                .distanciaKm(dto.distanciaKm())
                .build();
    }
}
