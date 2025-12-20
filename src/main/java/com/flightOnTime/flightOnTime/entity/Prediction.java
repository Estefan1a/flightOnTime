package com.flightOnTime.flightOnTime.entity;

import com.flightOnTime.flightOnTime.enums.PredictionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "predictios")
public class Prediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private PredictionStatus prevision;
    private Double probabilidad;
    private LocalDateTime fechaPrediccion;
    @OneToOne(optional = false)
    @JoinColumn(name = "flight_request_id")
    private FlightRequest flightRequest;

}
