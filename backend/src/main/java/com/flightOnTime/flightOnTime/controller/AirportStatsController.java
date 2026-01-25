package com.flightOnTime.flightOnTime.controller;

import com.flightOnTime.flightOnTime.dto.AirportStatsDTO;
import com.flightOnTime.flightOnTime.service.AirportStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stats/airports")
@RequiredArgsConstructor
@CrossOrigin("${frontend.url}")
public class AirportStatsController {
    private final AirportStatsService airportStatsService;

    @GetMapping("/top")
    public ResponseEntity<List<AirportStatsDTO>> getTopAirports() {
        return ResponseEntity.ok(airportStatsService.getTop5Airports());
    }
}