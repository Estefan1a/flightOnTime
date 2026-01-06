package com.flightOnTime.flightOnTime.controller;

import com.flightOnTime.flightOnTime.dto.PredictionStatsDTO;
import com.flightOnTime.flightOnTime.service.PredictionStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class PredictionStatsController {

    private final PredictionStatsService statsService;

    @GetMapping
    public ResponseEntity<PredictionStatsDTO> getStats() {
        return ResponseEntity.ok(statsService.getStats());
    }
}
