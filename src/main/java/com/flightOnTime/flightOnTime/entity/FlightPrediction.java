package com.flightOnTime.flightOnTime.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table (name = "flightpredictions")
@Entity(name = "FlightPredictions")
public class FlightPrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String aerolinea;
    private String origen;
    private String destino;
    private LocalDateTime fechaDePartida;
    private Integer distanciaEnKm;
}
