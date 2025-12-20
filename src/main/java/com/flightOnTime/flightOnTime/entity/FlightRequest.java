package com.flightOnTime.flightOnTime.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "flight_request")
@Builder
public class FlightRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String aerolinea;
    private String origen;
    private String destino;
    private LocalDateTime fechaPartida;
    private Integer distanciaKm;
}
